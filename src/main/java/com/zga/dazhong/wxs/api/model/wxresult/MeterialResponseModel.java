package com.zga.dazhong.wxs.api.model.wxresult;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeterialResponseModel extends WxBaseDomain {

    @JSONField(name = "type")
    private String type;
    @JSONField(name = "media_id")
    private String mediaId;
    @JSONField(name = "created_at")
    private String createdTime;

}
