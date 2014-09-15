package com.hisense.hitv.service;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisense.hitv.compress.ICompressLogFile;
import com.hisense.hitv.util.LogFileConfig;
import com.hisense.hitv.util.LogFileName;

/**
 * 重解析日志服务类
 * @author tianyuqi
 */
public class LogReparseService {
    private static Log LOG = LogFactory.getLog(LogReparseService.class);

    /**
     * 日志文件操作对象
     */
    private LogFileConfig logFileConfig;
    /**
     * 文件压缩处理对象
     */
    private ICompressLogFile compressLogFile;

    /**
     * @param logFileConfig the logFileConfig to set
     */
    public void setLogFileConfig(LogFileConfig logFileConfig) {
        this.logFileConfig = logFileConfig;
    }
    
    /**
     * @param compressLogFile the compressLogFile to set
     */
    public void setCompressLogFile(ICompressLogFile compressLogFile) {
        this.compressLogFile = compressLogFile;
    }
    
    /**
     * 日志重解析
     */
    public void doService() {
        LOG.info("==============LogReparseService start!");
        String redoFileBackupPath = logFileConfig.getRedoFileBackupPath();
        String logFilePath = logFileConfig.getLogFilePath();
        String backUpFileExtension = logFileConfig.getBackUpFileExtension();
        LogFileName fileNames = new LogFileName();
        List<String> files = fileNames.getFileListByPath(redoFileBackupPath);
        // 循环redoFileBackupPath下的文件，进行解压缩到日志解析目录
        for (int i = 0; i < files.size(); i++) {
            String filePath = files.get(i);
            if (filePath.endsWith(backUpFileExtension)) {
                File file = new File(filePath);
                String fileName = file.getName();
                if (compressLogFile.decompress(
                    fileName.substring(0, fileName.lastIndexOf(backUpFileExtension)), 
                    file.getPath(), logFilePath + "/" + file.getParentFile().getName())) {
                    LOG.info(fileName + " was reparsed OK!");
                    // 如果解压缩到日志解析目录成功，则删除文件
                    file.delete();
                };
            }
        }
        LOG.info("==============LogReparseService end!");
    }

}
