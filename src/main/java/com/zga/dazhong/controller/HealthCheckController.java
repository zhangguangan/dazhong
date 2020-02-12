package com.zga.dazhong.controller;


import com.zga.dazhong.common.redis.RedisCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangguangan
 *
 */
@RestController
@Log4j2
public class HealthCheckController {


    @RequestMapping({"status", "hello"})
    public String hello() {
        log.info("status ok");
        return "hello";
    }
}
