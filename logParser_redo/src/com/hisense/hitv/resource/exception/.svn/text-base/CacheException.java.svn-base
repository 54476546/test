package com.hisense.hitv.resource.exception;

/**
 * 异常类，主要用于cache数据取得失败场合。
 * errType：异常类型；errMsg：错误内容。
 * @author zhoudi
 * @version 1.0
 */
public class CacheException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    //异常信息
    private String message = null;
    //异常类型
    private String errType = null;

    /**
     * 构造函数
     * @param errMsg 异常信息
     * @param errType 异常类型
     */
    public CacheException(String errType, String errMsg) {
        this.setMessage(errMsg);
        this.setErrType(errType);
    }

    /**
     * @param errType the errType to set
     */
    public void setErrType(String errType) {
        this.errType = errType;
    }

    /**
     * @return the errType
     */
    public String getErrType() {
        return errType;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
}
