package com.zga.dazhong.fileOp;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangguangan on 2020/3/27
 * description:
 */
@Getter
@Setter
public class RuleModel {
    private String fieldName;
    // 0-String 1-int 2-date 3-decimal
    private int fieldDataType;
}
