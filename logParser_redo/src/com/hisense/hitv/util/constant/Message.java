/**
 * 
 */
package com.hisense.hitv.util.constant;

/**
 * Message定义
 * @author zhoudi
 */
public interface Message {
    /**
     * property项目format格式化失败错误信息
     */
    public static final String FORMAT_ERR =
        "{0} View log line:{1} Parse {2} error ! file is {3}.";
    /**
     * property项目cache中取值失败错误信息
     */
    public static final String COMM_ERR =
        "{0} View log line:{1} {2}! file is {3}.";
    /**
     * 具体项目在cache中取数据失败错误信息
     */
    public static final String CACHE_ERR_INFO =
        "{0} is invalid.";
    /**
     * isUnique验证失败错误信息
     */
    public static final String UNIQUE_ERR =
        "{0} View log line:{1} {2} have existed! file is {3}.";
    /**
     * AllEmpty验证失败错误信息
     */
    public static final String ALLEMPTY_ERR =
        "{0} View log line:{1} {2} are all empty! file is {3}.";
    /**
     * 日志项目内容超过最大长度
     */
    public static final String MAX_LEN_ERR =
        "{0} View log line:{1} the length of {2} is too long  ! file is {3}.";
    /**
     * endTime<startTime时错误信息
     */
    public static final String TIME_COM_ERR =
        "{0} View log line:{1} start time equals end time error!  file is {4}.";
    /**
     * 日志项目内容为空
     */
    public static final String NULL_VALUE_ERR =
        "{0} View log line:{1} the value of {2} is empty! file is {3}.";
    /**
     * 信息头
     */
    public static final String CANNOTPARSEBYPARAM = "Can not parse the value by param ";
    /**
     * 叹号
     */
    public static final String EXCLAMATION = "!";
}
