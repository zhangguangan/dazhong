package com.zga.dazhong.wxs.modules.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WxMaterial extends BaseEntity {

    private Integer materialType;

    private String materialWxId;

    private String materialWxUrl;

    private String materialLocalUrl;

    private String materialDescription;

    private String materialVid;

    private String materialCoverUrl;
}