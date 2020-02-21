package com.zga.dazhong.wxs.api.model.wxrequest;

import com.zga.dazhong.wxs.api.model.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserInfoDomain extends BaseDomain {

    @NotEmpty(message = "姓名不能为空")
    private String userName;

    @NotEmpty(message = "证件号不能为空")
    private String idNo;

    private String email;
}
