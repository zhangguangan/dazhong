package com.zga.dazhong.wxs.api.model.wxresult;

import com.alibaba.fastjson.annotation.JSONField;

public class WxBaseDomain extends WxResult {

    private static final long serialVersionUID = 9003772304220224540L;
    private String appId;
    private String secret;
    @JSONField(name = "openid")
    private String openId;
    @JSONField(name = "access_token")
    private String accessToken;
    @JSONField(name = "expires_in")
    private Integer expiresIn;
    private String lang = "zh_CN";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }
}
