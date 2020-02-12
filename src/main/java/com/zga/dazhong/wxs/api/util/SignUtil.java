package com.zga.dazhong.wxs.api.util;

import com.zga.dazhong.wxs.api.model.wxresult.WeChatSign;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignUtil {
    public static String token="legendsneverdie";
    /**
     * 微信token校验
     *
     * @param weChatSign
     * @return
     */
    public static boolean checkSignNature(WeChatSign weChatSign) {
        //对token，timestamp，nonce按字典排序
        String[] paramArr = new String[]{SignUtil.token, weChatSign.getTimestamp(), weChatSign.getNonce()};
        Arrays.sort(paramArr);

        String content = "";
        for (String target : paramArr) {
            //排序后的结果拼成字符串
            content += target;
        }

        String cipherText = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            //拼接后的字符串SHA-1加密
            byte[] digest = md.digest(content.toString().getBytes());
            cipherText = byteToStr(digest);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

//将SHA-1加密后的字符串与signature进行对比
        return cipherText != null ? cipherText.equals(weChatSign.getSignature().toUpperCase()) : false;
    }


    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;

    }

    /**
     * 将字节转换为十六进制字符串.
     *
     * @param mByte
     * @return
     */


    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

}
