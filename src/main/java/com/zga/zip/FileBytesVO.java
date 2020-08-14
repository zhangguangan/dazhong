package com.zga.zip;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangguangan on 2020/7/24
 * description:
 */
@Getter
@Setter
public class FileBytesVO {
    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件字节数组
     */
    private byte[] bytes;

    public FileBytesVO() {
    }

    public FileBytesVO(String fileName, byte[] bytes) {
        this.fileName = fileName;
        this.bytes = bytes;
    }
}
