package com.zga.dazhong.wxs.web;

import com.zga.dazhong.wxs.api.model.wxresult.WeChatSign;
import com.zga.dazhong.wxs.api.util.SignUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@Log4j2
public class TokenValidateController {


    /**
     * 微信请求校验
     *
     * @param
     */
    @RequestMapping(value = {"/wx"}, method = {RequestMethod.GET})
    public void checkToken(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //初始化
        WeChatSign weChatSign = new WeChatSign();
        weChatSign.setNonce(request.getParameter("nonce"));
        weChatSign.setSignature(request.getParameter("signature"));
        weChatSign.setTimestamp(request.getParameter("timestamp"));

        PrintWriter out = response.getWriter();
        //随机字符串
        String echostr = request.getParameter("echostr");
        //若校验成功则返回echostr
        if (SignUtil.checkSignNature(weChatSign)) {
            log.info("===========请求校验成功========");
            out.print(echostr);
        } else {
            log.info("===========请求校验失败========");
        }

        out.close();
        out = null;
    }
}
