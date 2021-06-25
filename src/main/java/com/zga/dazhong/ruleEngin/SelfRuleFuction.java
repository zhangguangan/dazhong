package com.zga.dazhong.ruleEngin;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

public class SelfRuleFuction extends AbstractFunction {
    @Override

    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2,
                              AviatorObject arg3) {
        long a = FunctionUtils.getNumberValue(arg1, env).longValue();
        long b = FunctionUtils.getNumberValue(arg2, env).longValue();
        long c = FunctionUtils.getNumberValue(arg3, env).longValue();
        return AviatorLong.valueOf(a + b + c);
    }

    @Override
    public String getName() {
        return "sum";
    }
}
