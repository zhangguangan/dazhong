package com.zga.leetcode;

/**
 * Created by zhangguangan on 2020/4/10
 * description:
 */
public class ReverseWordsSolution {
    public static void main(String[] args) {
        String result = new ReverseWordsSolution().reverseWords(" hello word ");
        System.out.println(result);
    }
    public String reverseWords(String s) {
        if (s == null || s.equals("")) {
            return "";
        }
        String[] words = s.trim().split(" ");
        StringBuffer sb = new StringBuffer();
        for (int i = words.length - 1; i >= 0 ; i--) {
            final String word = words[i];
            if (word.equals("")) {
                continue;
            }
            sb.append(word);
            if (i != 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
