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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public String uploadImageToWechat(HttpServletRequest request, @RequestParam(value = "picFile", required = false) MultipartFile picFile) {
        try {
//            String result = materialFacade.uploadMeterial(picFile.getInputStream(), picFile.getOriginalFilename());
            String result = getTxtContent(picFile);
            return result;
        } catch (Exception e) {
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

    private String getTxtContent(MultipartFile authorizeFile) {
        StringBuilder sb = new StringBuilder("");
        InputStream inputStream = null;
        try {
            inputStream = authorizeFile.getInputStream();
            byte[] tempBytes = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(tempBytes)) != -1) {
                String str = new String(tempBytes, 0, bytesRead);
                sb.append(str);
            }
        } catch (Exception e) {
            log.error("读取授权文件遇到异常，异常信息：", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("关闭授权文件输入流遇到异常，异常信息：", e);
                }
            }
        }

        return sb.toString();
    }
}
