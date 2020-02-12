package com.zga.dazhong.common.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.apache.commons.io.IOUtils;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HttpClientTemplate
 *
 * @author dingzeshuan
 * @date 2019/4/26 14:21
 */
@Log4j2
@Service
public class HttpClientTemplate {

    @Resource
    private CloseableHttpClient closeableHttpClient;

    public <T> T get(final String url, final Class<T> clazz) {
        String response = getForString(url);
       return JSONObject.parseObject(response, clazz);
    }

    /**
     * 为调用头条服务定义
     */
    public String getForString(String url, String token) {
        log.info("get请求地址：{}", url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Access-Token", token);
        return executeForString(httpGet, Charset.defaultCharset());
    }

    public String getForString(final String url) {
        log.info("get请求地址：{}", url);
        HttpGet httpGet = new HttpGet(url);
        return executeForString(httpGet, Charset.defaultCharset());
    }

    public byte[] getForByte(final String url) {
        log.info("get请求地址：{}", url);
        HttpGet httpGet = new HttpGet(url);
        return executeForByte(httpGet, Charset.defaultCharset());
    }

    public <T> T post(final String url, final String paramJson, final Class<T> clazz) {
        String response = postForJson(url, paramJson);
        return JSONObject.parseObject(response, clazz);
    }

    public String postForJson(final String url, final String paramJson) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("cache-control", "no-cache");
        headerMap.put("Accept", "*/*");
        return postForString(url, headerMap, paramJson);
    }
    public <T> T postFile(InputStream inputStream, final String url, String fileName, final Class<T> clazz) {
        HttpPost httpPost = new HttpPost(url);
        ByteArrayBody byteArrayBody = null;
        try {
            byteArrayBody = new ByteArrayBody(IOUtils.toByteArray(inputStream), fileName);
        } catch (Exception e) {
            log.error("发送文件失败");
            return null;
        }
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("media", byteArrayBody)
                .build();

        httpPost.setEntity(reqEntity);
        try {
            CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            InputStream content = httpResponse.getEntity().getContent();
            String response = IOUtils.toString(content, "utf-8");
            log.info("发送文件响应结果：{}", response);
            return JSONObject.parseObject(response, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String postForString(final String url, final Map<String, String> headerMap, final String paramJson) {
        log.info("post请求地址：{}", url);
        HttpPost httpPost = new HttpPost(url);
        Header[] headers = headerMap.entrySet().stream()
                .map(e -> new BasicHeader(e.getKey(), e.getValue())).collect(Collectors.toList())
                .toArray(new Header[headerMap.size()]);
        httpPost.setHeaders(headers);
        httpPost.setEntity(new StringEntity(paramJson, Charset.defaultCharset()));
        return executeForString(httpPost, Charset.defaultCharset());
    }

    public String executeForString(final HttpUriRequest request, Charset charset) {
        try(CloseableHttpResponse httpResponse =  closeableHttpClient.execute(request)) {
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status >= 200 && status < 400) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, charset);
                }
            }
            log.error("http(s)请求失败：url : {}, response: {}", request.getURI(),  httpResponse);
        } catch (IOException e) {
            log.error("http(s)请求异常: url: {}", request.getURI(), e);
        }
        return null;
    }

    public byte[] executeForByte(final HttpUriRequest request, Charset charset) {
        try(CloseableHttpResponse httpResponse =  closeableHttpClient.execute(request)) {
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status >= 200 && status < 400) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                    byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
                    int rc = 0;
                    while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                        swapStream.write(buff, 0, rc);
                    }
                    byte[] in_b = swapStream.toByteArray();
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(swapStream);
                    return in_b;
                }
            }
            log.error("http(s)请求失败：url : {}, response: {}", request.getURI(),  httpResponse);
        } catch (IOException e) {
            log.error("http(s)请求异常: url: {}", request.getURI(), e);
        }
        return null;
    }

    public void setCloseableHttpClient(CloseableHttpClient closeableHttpClient) {
        this.closeableHttpClient = closeableHttpClient;
    }
}
