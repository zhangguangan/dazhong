package com.zga.dazhong.wxs.modules.entity;

import com.alibaba.fastjson.JSON;
import com.zga.dazhong.wxs.api.model.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by zhangguangan on 2020/2/26
 * description:
 */
@Getter
@Setter
public class BaseEntity extends BaseDomain {
    private static final long serialVersionUID = 7952514626314547466L;
    private Long id;

    private Date dateCreated;

    private String createdBy;

    private Date dateUpdated;

    private String updatedBy;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}