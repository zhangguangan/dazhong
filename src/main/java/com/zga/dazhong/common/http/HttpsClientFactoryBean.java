package com.zga.dazhong.common.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

@Service("closeableHttpClient")
public class HttpsClientFactoryBean implements FactoryBean<CloseableHttpClient> {

    private Resource certResource;
    /**
     * 连接池大小
     */
    private int cmPoolSize = 10;
    /**
     * 从池中获取连接超时时间
     */
    private int connectionRequestTimeout = 3000;
    /**
     * 请求建立连接超时时间
     */
    private int connectionTimeout = 5000;
    /**
     * 请求响应超时时间
     */
    private int socketTimeout = 60000;
    /**
     * http连接是否复用
     */
    private boolean connectionReuse = true;

    /**
     * ssl版本
     */
    private String sslContextVertion = "TLS";

    private volatile CloseableHttpClient closeableHttpClient = null;

    @Override
    public CloseableHttpClient getObject() throws Exception {
        if (null == closeableHttpClient) {
            synchronized (HttpsClientFactoryBean.class) {
                if (null == closeableHttpClient) {
                    PoolingHttpClientConnectionManager httpClientConnectionManager;
                    if (certResource != null) {
                        Certificate certificate;
                        try (InputStream inputStream = certResource.getInputStream()) {
                            certificate = CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
                        }
                        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                        ks.load(null, null);
                        ks.setCertificateEntry(Integer.toString(1), certificate);

                        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                        tmf.init(ks);

                        SSLContext sslContext = SSLContext.getInstance(sslContextVertion);
                        sslContext.init(null, tmf.getTrustManagers(), null);

                        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                                sslContext,
                                new String[]{"TLSv1"},
                                null,
                                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

                        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslConnectionSocketFactory).build();
                        httpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
                    } else {
                        httpClientConnectionManager = new PoolingHttpClientConnectionManager();
                    }

                    httpClientConnectionManager.setMaxTotal(cmPoolSize);
                    httpClientConnectionManager.setDefaultMaxPerRoute(cmPoolSize);
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setConnectTimeout(connectionTimeout)
                            .setSocketTimeout(socketTimeout)
                            .setConnectionRequestTimeout(connectionRequestTimeout)
                            .build();

                    HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(httpClientConnectionManager)
                            .setDefaultRequestConfig(requestConfig)
                            .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
                    if (!connectionReuse) {
                        httpClientBuilder.setConnectionReuseStrategy(new NoConnectionReuseStrategy());
                    }
                    closeableHttpClient = httpClientBuilder.build();
                }
            }
        }
        return closeableHttpClient;
    }

    public String getSslContextVertion() {
        return sslContextVertion;
    }

    public void setSslContextVertion(String sslContextVertion) {
        this.sslContextVertion = sslContextVertion;
    }

    @Override
    public Class<?> getObjectType() {
        return CloseableHttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public int getCmPoolSize() {
        return cmPoolSize;
    }

    public void setCmPoolSize(int cmPoolSize) {
        this.cmPoolSize = cmPoolSize;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Resource getCertResource() {
        return certResource;
    }

    public void setCertResource(Resource certResource) {
        this.certResource = certResource;
    }

    public Boolean getConnectionReuse() {
        return connectionReuse;
    }

    public void setConnectionReuse(Boolean connectionReuse) {
        this.connectionReuse = connectionReuse;
    }
}
