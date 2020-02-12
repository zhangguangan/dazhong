package com.zga.dazhong.wxs.web;

import com.alibaba.fastjson.JSONObject;
import com.zga.dazhong.wxs.wxs.AccessTokenFacade;
import com.zga.dazhong.wxs.wxs.MaterialFacade;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;

@RestController
@Log4j2
public class WXWebController {
    @Resource
    private AccessTokenFacade accessTokenFacade;
    @Resource
    private MaterialFacade materialFacade;

    @RequestMapping("accesstoken")
    public String getAccesstoken() {
        return accessTokenFacade.getAccessToken();
    }
    @RequestMapping("/uploadImageToWechat")
    public String uploadImageToWechat(HttpServletRequest request, @RequestParam(value = "picFile", required = false) MultipartFile picFile){
       try {
           String result=materialFacade.uploadMeterial(picFile.getInputStream(), picFile.getOriginalFilename());
           return result;
       } catch (Exception e){
           log.error("error", e);
           return "err";
       }
    }

    @RequestMapping("/getMeterial")
    public String getMeterial() {
        String mid = "Qw2zlC4QNBc85bIQYY9ZMISCK5jg-LgV_oIZYr1P3nSiWKHcuZFA_vYVRn7uOvuT";
        String result = materialFacade.getMeterial(mid);
        return result;
    }
}
