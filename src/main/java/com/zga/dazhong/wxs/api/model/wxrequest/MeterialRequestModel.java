package com.zga.dazhong.wxs.api.model.wxrequest;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeterialRequestModel {
    @JSONField(name = "access_token")
    private String accessToken;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "media")
    private String fileName;

}
