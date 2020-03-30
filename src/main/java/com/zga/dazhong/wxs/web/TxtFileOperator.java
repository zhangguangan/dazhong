package com.zga.dazhong.wxs.web;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangguangan on 2020/3/26
 * description:
 */
@Log4j2
public class TxtFileOperator {
    private final static String FILE_PATH = "D:\\需求\\tidb数据切换\\Fwd__申请导出pgxl数据\\aaa.txt";

    public static void main(String[] args) {
        List<String> contentList = getTxtContent(FILE_PATH);
        StringBuffer baseSql = new StringBuffer("insert into m_coupon_daily_info (activity_name, coupon_name, stat_date, activity_code, coupon_code,coupon_type, receive_num, use_num, estimate_amount, actual_num, actual_amount, unused_amount)values ");
        StringBuffer sql = new StringBuffer("");
        for (int i = 1; i < contentList.size(); i++) {
            sql.append(baseSql);
            String content = contentList.get(i);
            sql.append("(");
            sql = buildSql(sql, content);
            sql.append(");\n");
        }
        String result = sql.toString();
//        System.out.println(result);
        write(result);
        System.out.println(result.length());

    }

    private static StringBuffer buildSql(StringBuffer sql, String content) {
        String[] contentArray = content.split("\t");
        for (int i = 0; i < contentArray.length; i++) {
            if (contentArray[i].trim().equals("\"")) {
                continue;
            }
            if (i < 5) {
                if (contentArray[i].equals("NULL")) {
                    sql.append("NULL");
                } else {
                    sql.append("'").append(contentArray[i]).append("'");
                }
            } else {
                sql.append(contentArray[i]);
            }
            if (i != contentArray.length - 1) {
                sql.append(", ");
            }
        }
        return sql;
    }


    public static void write(String result) {
        FileWriter fw = null;
        try {
            File file = new File("D:\\需求\\tidb数据切换\\Fwd__申请导出pgxl数据\\fff.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getPath(), true);
            fw.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> getTxtContent(String filePath) {
        File authorizeFile = new File(filePath);
        List<String> contentList = new ArrayList<>(1000);
        InputStream inputStream = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(authorizeFile));
            String lineTxt = reader.readLine();
            while (StringUtils.isNotEmpty(lineTxt)) {
                contentList.add(lineTxt);
                lineTxt = reader.readLine();
            }
        } catch (Exception e) {
            log.error("读取授权文件遇到异常，异常信息：", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("关闭授权文件输入流遇到异常，异常信息：", e);
                }
            }
        }

        return contentList;
    }
}
