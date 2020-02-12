package com.zga.dazhong.wxs.api.model.wxresult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeChatSign {
    private String nonce;
    private String signature;
    private String timestamp;

}
