package com.zga.zip;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by zhangguangan on 2020/7/24
 * description:
 */
@Log4j2
@Service
public class ZipOperator {
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    /**
     * 写文件bytes到zip
     *
     * @param voList  {文件名，文件内容数组} 列表
     * @param outputStream outputStream
     * @param charset 字符编码
     */
    public static void writeFilesStream2Zip(List<FileBytesVO> voList, OutputStream outputStream, Charset charset) {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, charset);
            for (FileBytesVO fileBytesVO : voList) {
                String fileName = fileBytesVO.getFileName();
                if (StringUtils.isEmpty(fileName)) {
                    continue;
                }

                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(fileBytesVO.getBytes(), 0, fileBytesVO.getBytes().length);
                zipOutputStream.closeEntry();
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
        } catch (Exception e) {
            log.error("写出zip遇到异常，异常信息：" + e);
        }
    }


/*    public static void main(String[] args) {
        List<FileBytesVO> voList = Arrays.asList(new FileBytesVO("a.txt", "测试a".getBytes()),
                new FileBytesVO("b.txt", "测试b".getBytes(CHARSET_UTF8)),
                new FileBytesVO("c.txt", "测试c".getBytes(CHARSET_UTF8)),
                new FileBytesVO("d.txt", "测试d".getBytes(CHARSET_UTF8)));

        try {
            writeFilesStream2Zip(voList, new BufferedOutputStream(new FileOutputStream(zipName)), CHARSET_UTF8);
        } catch (Exception e) {
            log.error(e);
        }
    }*/

}
