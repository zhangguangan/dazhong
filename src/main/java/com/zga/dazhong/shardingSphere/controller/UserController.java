package com.zga.dazhong.shardingSphere.controller;

import com.zga.dazhong.shardingSphere.entity.UserInfo;
import com.zga.dazhong.shardingSphere.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {
    @Resource
    private UserInfoService userService;

    @GetMapping("/select")
    public List<UserInfo> select(@RequestBody UserInfo requestModel) {
        return userService.getUserList(requestModel);
    }

    @GetMapping("/insert")
    public int insert(UserInfo user) {
        return userService.save(user);
    }

}
