package com.zga.dazhong.wxs.api.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class BaseDomain implements Serializable {
    public BaseDomain() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

