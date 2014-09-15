package com.hisense.hitv.log.logfile.domain;

import java.util.Date;

/**
 * LOGFIlE表结构对象
 * @author zhoudi
 * @version 1.0
 */
public class LogFileVO {

    private String fileName;
    private long fileSize;
    private int recordNumber;
    private Date processStartDate;
    private Date processEndDate;
    private Integer errorNumber;
    private int logFileType;

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the recordNumber
     */
    public int getRecordNumber() {
        return recordNumber;
    }

    /**
     * @param recordNumber the recordNumber to set
     */
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    /**
     * @return the processEndDate
     */
    public Date getProcessEndDate() {
        return processEndDate;
    }

    /**
     * @param processEndDate the processEndDate to set
     */
    public void setProcessEndDate(Date processEndDate) {
        this.processEndDate = processEndDate;
    }

    /**
     * @return the logFileType
     */
    public int getLogFileType() {
        return logFileType;
    }

    /**
     * @param logFileType the logFileType to set
     */
    public void setLogFileType(int logFileType) {
        this.logFileType = logFileType;
    }

    /**
     * @return the processStartDate
     */
    public Date getProcessStartDate() {
        return processStartDate;
    }

    /**
     * @param processStartDate the processStartDate to set
     */
    public void setProcessStartDate(Date processStartDate) {
        this.processStartDate = processStartDate;
    }
    
    /**
     * @return the errorNumber
     */
    public int getErrorNumber() {
        return errorNumber;
    }

    /**
     * @param errorNumber the errorNumber to set
     */
    public void setErrorNumber(Integer errorNumber) {
        this.errorNumber = errorNumber;
    }

}
