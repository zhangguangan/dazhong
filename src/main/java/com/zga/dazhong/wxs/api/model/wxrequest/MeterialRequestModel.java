package com.zga.dazhong.wxs.api.model.wxrequest;

import com.alibaba.fastjson.annotation.JSONField;
import com.zga.dazhong.wxs.api.model.BaseDomain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeterialRequestModel extends BaseDomain {
    @JSONField(name = "access_token")
    private String accessToken;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "media")
    private String fileName;

}
