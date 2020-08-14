package com.zga.dazhong.controller;

import com.zga.zip.FileBytesVO;
import com.zga.zip.ZipOperator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangguangan on 2020/7/24
 * description:
 */
@Controller
@Log4j2
public class ZipOperatorController {
    private ZipOperator zipOperator = new ZipOperator();

    @RequestMapping("downloadFiles")
    public void downloadFiles(HttpServletRequest request, HttpServletResponse response) {
        List<FileBytesVO> voList = Arrays.asList(new FileBytesVO("a.txt", "测试a".getBytes()),
                new FileBytesVO("b.txt", "测试b".getBytes(zipOperator.CHARSET_UTF8)),
                new FileBytesVO("c.txt", "测试c".getBytes(zipOperator.CHARSET_UTF8)),
                new FileBytesVO("d.txt", "测试d".getBytes(zipOperator.CHARSET_UTF8)));

        downloadFileListVO(voList, response);
    }

    private void downloadFileListVO(List<FileBytesVO> voList, HttpServletResponse response) {
        OutputStream out = null;
        try {
            String headStr = "attachment; filename=\"" + "zipOperator.zip" + "\"";
            response.setContentType("APPLICATION/zip");
            response.setHeader("Content-Disposition", headStr);
            out = response.getOutputStream();
            zipOperator.writeFilesStream2Zip(voList, out, zipOperator.CHARSET_UTF8);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                } catch (Exception e) {
                    log.error("关闭输出流遇到异常，异常信息：", e);
                }
                try {
                    out.close();
                } catch (Exception e) {
                    log.error("关闭输出流遇到异常，异常信息：", e);
                }
            }
        }
    }
}
