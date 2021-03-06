package com.hisense.hitv.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 日志文件操作类，包含日志文件路径、文件名、文件后缀等操作
 * @author zhoudi
 * @version 1.0
 */
public class LogFileConfig {

    /**
     * point
     */
    private static final String POINT = ".";
    /**
     * slash
     */
    private static final String SLASH = "/";
    private String logFilePath;
    private String fileBackupPath;
    private String redoFileBackupPath;
    private String errorFileBackupPath;
    private String logFileType;
    private String zipFileType;

    private String filePath;
    private Properties properties;
    private Map<String, String> configs;    

    /**
     * constructor function
     * @param filePath file path
     */
    public LogFileConfig(String filePath) {
        this.filePath = filePath;
        properties = new Properties();
        try {
            InputStream in = new FileInputStream(filePath);
            properties.load(in);
            configs = new HashMap<String, String>();
            for (String key : properties.stringPropertyNames()) {
                configs.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            // empty
        }
    }
    
    /**
     * @return the configs
     */
    public Map<String, String> getConfigs() {
        return configs;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * get property value by property name
     * @param key property name
     * @return the properties
     */
    public String getProperties(String key) {
        return (String) properties.get(key);
    }

    /**
     * get file encoding by propertyname
     * @param key propertyname
     * @return file encoding
     */
    public String getFileEncoding(String key) {
        return (String) properties.get(key + "_encoding");
    }

    /**
     * @param logFilePath the logFilePath to set
     */
    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    /**
     * @param fileBackupPath the fileBackupPath to set
     */
    public void setFileBackupPath(String fileBackupPath) {
        this.fileBackupPath = fileBackupPath;
    }

    /**
     * @return redoFileBackupPath 
     */
    public String getRedoFileBackupPath() {
        return redoFileBackupPath;
    }

    /**
     * @param redoFileBackupPath the redoFileBackupPath to set
     */
    public void setRedoFileBackupPath(String redoFileBackupPath) {
        this.redoFileBackupPath = redoFileBackupPath;
    }

    /**
     * @param errorFileBackupPath the errorFileBackupPath to set
     */
    public void setErrorFileBackupPath(String errorFileBackupPath) {
        this.errorFileBackupPath = errorFileBackupPath;
    }

    /**
     * @param logFileType the logFileType to set
     */
    public void setLogFileType(String logFileType) {
        this.logFileType = logFileType;
    }

    /**
     * @param zipFileType the zipFileType to set
     */
    public void setZipFileType(String zipFileType) {
        this.zipFileType = zipFileType;
    }

    /**
     * @return the logFilePath
     */
    public String getLogFilePath() {
        return logFilePath;
    }

    /**
     * @return the fileBackupPath
     */
    public String getFileBackupPath() {
        return fileBackupPath;
    }

    /**
     * @return the errorFileBackupPath
     */
    public String getErrorFileBackupPath() {
        return errorFileBackupPath;
    }

    /**
     * @return the logFileType
     */
    public String getLogFileType() {
        return logFileType;
    }

    /**
     * @return the zipFileType
     */
    public String getZipFileType() {
        return zipFileType;
    }

    /**
     * @return the file extension
     */
    public String getFileNameExtension() {
        return POINT + getLogFileType(); // .log
    }

    /**
     * @return the backup file extension
     */
    public String getBackUpFileExtension() {
        return POINT + getZipFileType(); // .gz
    }

    /**
     * get log file dir by file category
     * @param fileCategory file category
     * @return log file dir
     */
    public String getLogFileFolderDir(String fileCategory) {
        return getLogFilePath() + SLASH + fileCategory;
    }

    /**
     * get backup file dir by file category
     * @param fileCategory category
     * @return backup file dir
     */
    public String getBackUpFileFolderDir(String fileCategory) {
        return getFileBackupPath() + SLASH + fileCategory;
    }

    /**
     * get error backup file dir by file category
     * @param fileCategroy file category
     * @return error backup file dir
     */
    public String getErrorBackUpFileFolderDir(String fileCategroy) {
        return getErrorFileBackupPath() + SLASH + fileCategroy;
    }
    
    /**
     * get redo backup file dir by file category
     * @param fileCategroy file category
     * @return redo backup file dir
     */
    public String getRedoBackupFileFolderDir(String fileCategroy) {
        return getRedoFileBackupPath() + SLASH + fileCategroy;
    }

    /**
     * @return the tmp file extension
     */
    public String getTmpFileNameExtension() {
        return ".tmp";
    }

    /**
     * get file time zone by propertyname
     * @param key propertyname
     * @return time zone
     */
    public String getFileTimeZone(String key) {
        return (String) properties.get(key + "_timezone");
    }
}
