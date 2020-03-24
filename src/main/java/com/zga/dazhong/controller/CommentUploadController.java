package com.zga.dazhong.controller;

import com.zga.dazhong.common.http.HttpClientTemplate;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangguangan on 2020/3/24
 * description:
 */
@RestController
@Log4j2
public class CommentUploadController {

    private final static String ARE_AADRESS = "http://mktmain-web-test.360jie.com.cn";
    private final static String COMMENT_URL = ARE_AADRESS + "/comment/addComment?commentContent=CONTENT_TEMPLATE&commentTopic=TECHNOLOGY_EPIDEMIC";
    private final static String CONTENT_TEMPLATE = "CONTENT_TEMPLATE";

    @Resource
    private HttpClientTemplate httpClientTemplate;


    @RequestMapping("/uploadComment")
    public String uploadComment(@RequestParam(value = "upload", required = false) MultipartFile commentFile) {
        List<String> contentList = getTxtContent(commentFile);

        log.info("初始化留言列表:{}", contentList);
        String result = "";
        // 组装请求发送请求
        for (String commentContent : contentList) {
            if (StringUtils.isEmpty(commentContent)) {
                continue;
            }
            try {
                String requestUrl = COMMENT_URL.replace(CONTENT_TEMPLATE, commentContent);
                String response = httpClientTemplate.get(requestUrl, String.class);
                result += response + '\n';
            } catch (Exception e) {
                log.error("新增留言遇到异常 ，异常项：{}", commentContent);
            }
        }
        return result;
    }


    private List<String> getTxtContent(MultipartFile authorizeFile) {
        List<String> contentList = new ArrayList<>(1000);
        InputStream inputStream = null;
        try {
            inputStream = authorizeFile.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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
