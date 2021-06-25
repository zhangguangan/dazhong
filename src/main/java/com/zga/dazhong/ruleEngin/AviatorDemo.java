package com.zga.dazhong.ruleEngin;

import com.googlecode.aviator.AviatorEvaluator;

import java.util.HashMap;
import java.util.Map;

public class AviatorDemo {
    public static void main(String[] args) {
        simpleExpressionDemo();

        selfFunctionDemo();
    }

    private static void selfFunctionDemo() {
        AviatorEvaluator.addFunction(new SelfRuleFuction());
        String expression = "sum(a, b, c)";
        Map<String, Object> valueMapping = new HashMap<>();
        valueMapping.put("a", 1);
        valueMapping.put("b", 2);
        valueMapping.put("c", 3);
        Long result = (Long) AviatorEvaluator.execute(expression, valueMapping);
        System.out.println("self function result:" + result);
    }

    private static void simpleExpressionDemo() {

        String expression = "a + b + c";
        Map<String, Object> valueMapping = new HashMap<>();
        valueMapping.put("a", 1);
        valueMapping.put("b", 2);
        valueMapping.put("c", 3);
        Long result = (Long) AviatorEvaluator.execute(expression, valueMapping);
        System.out.println("result:" + result);
    }
}
