package com.zga.dazhong.wxs.gws;

import com.zga.dazhong.common.http.HttpClientTemplate;
import com.zga.dazhong.wxs.api.model.wxresult.MeterialResponseModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@Log4j2
public class GwsMaterialFacade {
    @Resource
    private AccessTokenFacade accessTokenFacade;
    @Resource
    private HttpClientTemplate httpClientTemplate;

    public String uploadMaterial(byte[] fileBytes, String fileName, String fileType) {
        String url = WxUrlConstant.UPLOAD_MATERIAL_URL + "access_token="+accessTokenFacade.getAccessToken()+"&type=" + fileType;
        InputStream inputStream = new ByteArrayInputStream(fileBytes);
        MeterialResponseModel responseModel = httpClientTemplate.postFile(inputStream, url, fileName, MeterialResponseModel.class);
        return responseModel.getMediaId();
    }

    public String getMaterial(String materialId) {
        String url = WxUrlConstant.GET_METERIAL_URL + "access_token="+accessTokenFacade.getAccessToken()+"&media_id="+ materialId;
        String result = httpClientTemplate.getForString(url);
        log.info("获取的素材url: {}", result);
        return result;
    }

}
