package com.zga.dazhong.wxs.gws;

import com.zga.dazhong.common.http.HttpClientTemplate;
import com.zga.dazhong.wxs.api.model.wxresult.MeterialResponseModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

@Service
@Log4j2
public class MaterialFacade {
    @Resource
    private AccessTokenFacade accessTokenFacade;
    @Resource
    private HttpClientTemplate httpClientTemplate;

    public String uploadMeterial(InputStream inputStream, String fileName) {
        String url = WxUrlConstant.UPLOAD_MATERIAL_URL + "access_token="+accessTokenFacade.getAccessToken()+"&type=image";
        MeterialResponseModel responseModel = httpClientTemplate.postFile(inputStream, url, fileName, MeterialResponseModel.class);
        return responseModel.getMediaId();
    }

    public String getMeterial(String meterialId) {
        String url = WxUrlConstant.GET_METERIAL_URL + "access_token="+accessTokenFacade.getAccessToken()+"&media_id="+ meterialId;
        String result = httpClientTemplate.getForString(url);
        log.info("获取的素材url: {}", result);
        return result;
    }

}
