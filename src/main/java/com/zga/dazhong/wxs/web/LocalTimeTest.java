package com.zga.dazhong.wxs.web;

import java.time.LocalDate;

/**
 * Created by zhangguangan on 2020/5/6
 * description:
 */
public class LocalTimeTest {
    public static void main(String[] args) {
        LocalDate now = LocalDate.now();
        String beginDate = "2020-05-01";
        String endDate = "2020-05-06";
        LocalDate beginDt = LocalDate.parse(beginDate);
        LocalDate endDt = LocalDate.parse(endDate);

        System.out.println(now.isAfter(endDt));
    }
}
