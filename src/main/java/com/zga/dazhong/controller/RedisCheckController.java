package com.zga.dazhong.controller;

import com.zga.dazhong.common.redis.RedisCache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RedisCheckController {

    @Resource
    private RedisCache redisCache;

    @RequestMapping("/redis/set")
    public String redisSet() {
        redisCache.set("123", "456");
        return "set ok";
    }

    @RequestMapping("/redis/get")
    public String redisGet() {
        String value = redisCache.get("123");
        return "get ok, value is：" + value;
    }
}
