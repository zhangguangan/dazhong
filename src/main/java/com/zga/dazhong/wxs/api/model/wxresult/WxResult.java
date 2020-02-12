package com.zga.dazhong.wxs.api.model.wxresult;

import com.alibaba.fastjson.annotation.JSONField;
import com.zga.dazhong.wxs.api.model.BaseDomain;

import java.util.Objects;

public class WxResult extends BaseDomain {

    private static final long serialVersionUID = 7646556292629209646L;

    private static final Integer SUCCESS_CODE = 0;  //成功
    private static final Integer SYS_BUSY_CODE = -1;    //系统繁忙

    @JSONField(name = "errcode")
    private Integer errCode;
    @JSONField(name = "errmsg")
    private String errMsg;

    public boolean isErr() {
        if (Objects.isNull(errCode) || SUCCESS_CODE.equals(errCode)) {
            return false;
        }
        return true;
    }

    public boolean isBusy() {
        if (SYS_BUSY_CODE.equals(errCode)) {
            return true;
        }
        return false;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
