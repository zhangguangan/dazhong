package com.zga.dazhong.wxs.gws;

import com.zga.dazhong.common.http.HttpClientTemplate;
import com.zga.dazhong.common.redis.RedisCache;
import com.zga.dazhong.common.redis.RedisLockService;
import com.zga.dazhong.wxs.api.model.wxresult.WxBaseDomain;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Log4j2
@Service
public class AccessTokenFacade {
    @Resource
    private RedisCache redisCache;
    @Resource
    private RedisLockService redisLockService;
    @Resource
    private HttpClientTemplate httpClientTemplate;

    public String getAccessToken() {
        WxBaseDomain result = redisCache.get("accesstoken");
        if (result != null && StringUtils.isNotEmpty(result.getAccessToken())) {
            return result.getAccessToken();
        }
        try {
            redisLockService.lock("accesstokenLock", 3000);
            String url = WxUrlConstant.ACCESS_TOKEN_URL +
                    "?appid=" + WxUrlConstant.APP_ID +
                    "&secret=" + WxUrlConstant.APP_SECURT +
                    "&grant_type=client_credential";
            log.info("获取accessToken请求：{}", url);
            WxBaseDomain data = httpClientTemplate.get(url, WxBaseDomain.class);
            log.info("获取accessToken结果：{}", data);
            redisCache.set("accesstoken", data);
            return data.getAccessToken();
        } catch (Exception e) {
            log.error("获取accesstoken遇到异常异常信息：", e);
        } finally {
            redisLockService.unLock("accesstokenLock");
        }
        return null;
    }
}
