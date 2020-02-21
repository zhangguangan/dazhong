package com.zga.dazhong.wxs.web;

import com.zga.dazhong.wxs.api.model.wxrequest.UserInfoDomain;
import com.zga.dazhong.wxs.api.model.wxrequest.ValidateModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Log4j2
public class ValidationController {

    @RequestMapping("/validate")
    public String validate(@Valid @RequestBody ValidateModel requestModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.info(error.getDefaultMessage());
                return error.getDefaultMessage();
            }
        }
        log.info("请求数据：" + requestModel);
        return "ok";
    }

    @RequestMapping("/validateCopy")
    public String validateCopy() {
        UserInfoDomain userInfoDomain = new UserInfoDomain();
        userInfoDomain.setUserName("234");
        userInfoDomain.setIdNo("5467890");
        userInfoDomain.setEmail("6789098765678@");

        ValidateModel validateModel = new ValidateModel();
        BeanUtils.copyProperties(userInfoDomain, validateModel);
        log.info("copy: {}", validateModel);
        return "ok";
    }
}
