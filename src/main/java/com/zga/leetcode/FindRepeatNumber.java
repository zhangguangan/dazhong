package com.zga.leetcode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangguangan on 2020/4/7
 * description:
 */
public class FindRepeatNumber {
    public int findRepeatNumber(int[] nums) {
        int result = -1;
        Set<Integer> numberSet = new HashSet<>();
        for (int num : nums) {
            if (numberSet.contains(num)) {
                return num;
            } else {
                numberSet.add(num);
            }
        }

        return result;
    }
}
