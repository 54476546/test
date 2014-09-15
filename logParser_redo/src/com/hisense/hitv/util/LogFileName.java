package com.hisense.hitv.util;

import com.hisense.hitv.util.CalculateDate;
import com.hisense.hitv.filter.FileDirFilterImpl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件名相关操作类
 * @author zhoudi
 * @version 1.0
 */
public class LogFileName {
    private static final Log LOG = LogFactory.getLog(LogFileName.class);

    /**
     * 根据文件类型和扩扎名获取指定目录的文件名列表
     * @param filePath 文件目录
     * @param fileCategory 文件类型
     * @param fileExtension 文件扩展名
     * @return 文件名列表
     */
    public List<String> getFileName(String filePath, String fileCategory,
        String fileExtension) {
        File path = new File(filePath);

        // 过滤掉不符合规则的文件
        String[] files =
            path.list(new FileDirFilterImpl(fileCategory, fileExtension));
        if (files == null || files.length == 0) {
            LOG.debug(CalculateDate.todayMillisecond() + " " + fileCategory
                + "ViewLog: has no file!");
            return new ArrayList<String>();
        } else {
            Arrays.sort(files);
            return new ArrayList<String>(Arrays.asList(files));
        }
    }

    /**
     * 获取filePath下所有文件，不包括文件夹
     * @param filePath 文件路径
     * @return allFileList
     */
    public List<String> getFileListByPath(String filePath) {
        File file = new File(filePath);
        List<String> allFileList = new ArrayList<String>();
        // 如果该文件存在
        if (file.exists()) {
            // 如果是文件，输出目录路径
            if (!file.isDirectory()) {
                allFileList.add(file.getPath());
            // 如果不是文件，而是文件夹，则循环
            } else if (file.isDirectory()) {
                // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录。
                String[] fileList = file.list();
                if (fileList != null && fileList.length > 0) {
                    // 循环文件，或者是文件夹
                    for (int i = 0; i < fileList.length; i++) {
                        // 递归
                        allFileList.addAll(getFileListByPath(filePath + "/" + fileList[i]));
                    }
                }
            }
        }
        return allFileList;
    }
}
