package com.hisense.hitv.filter;

import java.io.FilenameFilter;
import java.io.File;

/**
 * 文件夹过滤器，实现java FilenameFilter接口
 * @author zhoudi
 * @version 1.0
 */
public class FileDirFilterImpl implements FilenameFilter {
    private String fileExtension;
    private String fileCategory;

    /**
     * 构造函数
     * @param fileCategory 文件类型
     * @param fileExtension 文件扩展名
     */
    public FileDirFilterImpl(String fileCategory, String fileExtension) {
        this.fileCategory = fileCategory;
        this.fileExtension = fileExtension;
    }

    /**
     * @param dir 文件对象
     * @param name 文件扩展名
     * @return  true：文件类型正确，false：文件类型错误
     */
    public boolean accept(File dir, String name) {
        boolean extensionFlag =
            name.toLowerCase().endsWith(fileExtension.toLowerCase());
        boolean prefixFlag =
            name.toLowerCase().startsWith(fileCategory.toLowerCase());

        return (prefixFlag && extensionFlag);
    }
}