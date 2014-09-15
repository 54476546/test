package com.hisense.hitv.util;

/**
 * 网元告警类别及异常常量类
 * @author zhoudi
 * @version 1.0
 */
public class AlarmDef {

    /**
     * 线程异常
     */
    public static final String THREADEXCEPTION = "1001";
    /**
     * 文件读取异常
     */
    public static final String READFILEEXCEPTION = "1002";
    /**
     * 开始时间格式化异常
     */
    public static final String STARTTIMEFORMATEXCEPTION = "1003";
    /**
     * Content找不到对应的Program异常
     */
    public static final String CONTENTHASNOTPROGRAMEXCEPTION = "1004";
    /**
     * 创建时间格式化异常
     */
    public static final String CREATEDATEFORMATEXCEPTION = "1005";
    /**
     * 插入操作异常
     */
    public static final String INSERTEXCEPTION = "1006";
    /**
     * merge操作异常
     */
    public static final String MERGEEXCEPTION = "1007";
    /**
     * 日志行内容异常
     */
    public static final String COLUMNISNOTCORRECTEXCEPTION = "1008";
    /**
     * 日期格式化异常
     */
    public static final String DATEFORMATEEXCEPTION = "1009";
    /**
     * 普通异常
     */
    public static final String EXCEPTION = "1100";
    /**
     * 日志类别定义
     */
    private static final String SUBMODULE = "00";
    private static final String EPGMODULE = "01";
    private static final String SUBEPGMODULE = SUBMODULE;
    private static final String MDCMODULE = "02";
    private static final String SUBMDCMODULE = SUBMODULE;
    private static final String PSMODULE = "03";
    private static final String SUBPSMODULE = SUBMODULE;
    private static final String TERMINATIONMODULE = "04";
    private static final String SUBTERMINATIONMODULE = SUBMODULE;
    private static final String STVCHANNELMODULE = "05";
    private static final String SUBSTVCHANNELMODULE = SUBMODULE;
    private static final String STVSERVICEMODULE = "06";
    private static final String SUBSTVSERVICEMODULE = SUBMODULE;
    private static final String VODMODULE = "07";
    private static final String SUBVODMODULE = SUBMODULE;
    private static final String TVODMODULE = "08";
    private static final String SUBTVODMODULE = SUBMODULE;
    private static final String ADMODULE = "09";
    private static final String SUBADMODULE = SUBMODULE;
    private static final String CHANNELMODULE = "10";
    private static final String SUBCHANNELMODULE = SUBMODULE;
    private static final String ADMACHINEMODULE = "11";
    private static final String SUBADMACHINEMODULE = SUBMODULE;
    private static final String STBSTATUSMODULE = "12";
    private static final String SUBSTBSTATUSMODULE = SUBMODULE;
    private static final String LOGXMLFILEPARSEMODULE = "13";
    private static final String SUBLOGXMLFILEPARSEMODULE = SUBMODULE;

    /**
     * constructor
     */
    private AlarmDef() {
    }

    /**
     * 获取告警ID
     * @param categryId 日志类别
     * @param exceptionId 异常ID
     * @return 告警ID
     */
    public static String getAlarm(String categryId, String exceptionId) {
        String alarm = null;
        if ("epg".equals(categryId)) {
            alarm = EPGMODULE + SUBEPGMODULE + exceptionId;
        } else if ("mdc".equals(categryId)) {
            alarm = MDCMODULE + SUBMDCMODULE + exceptionId;
        } else if ("ps".equals(categryId)) {
            alarm = PSMODULE + SUBPSMODULE + exceptionId;
        } else if ("termination".equals(categryId)) {
            alarm = TERMINATIONMODULE + SUBTERMINATIONMODULE + exceptionId;
        } else if ("vod".equals(categryId)) {
            alarm = VODMODULE + SUBVODMODULE + exceptionId;
        } else if ("tvod".equals(categryId)) {
            alarm = TVODMODULE + SUBTVODMODULE + exceptionId;
        } else if ("ad".equals(categryId)) {
            alarm = ADMODULE + SUBADMODULE + exceptionId;
        } else if ("channel".equals(categryId)) {
            alarm = CHANNELMODULE + SUBCHANNELMODULE + exceptionId;
        } else if ("admachine".equals(categryId)) {
            alarm = ADMACHINEMODULE + SUBADMACHINEMODULE + exceptionId;
        } else if ("stbstatus".equals(categryId)) {
            alarm = STBSTATUSMODULE + SUBSTBSTATUSMODULE + exceptionId;
        } else if ("stvchannel".equals(categryId)) {
            alarm = STVCHANNELMODULE + SUBSTVCHANNELMODULE + exceptionId;
        } else if ("stvservice".equals(categryId)) {
            alarm = STVSERVICEMODULE + SUBSTVSERVICEMODULE + exceptionId;
        } else if ("logxml".equals(categryId)) {
            alarm = LOGXMLFILEPARSEMODULE + SUBLOGXMLFILEPARSEMODULE + exceptionId;
        }
        return alarm;
    }

}
