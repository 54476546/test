package com.hisense.hitv.util;

import java.util.Map;

/**
 * 公共工具类，提供各种字符串、检查等处理
 * @author zhoudi
 * @version 1.0
 */
public class CommonUtil {

    /**
     * 
     */
    private static final int ORACLE_CHINESE_BYTE = 3;
    /**
     * 
     */
    private static final int MAX_ASCII = 255;

    /**
     * constructor
     */
    private CommonUtil() {
    }
    
    /**
     * 检查所传参数是否在Map中存在
     * @param recordMap 检查Map
     * @param checkObj 检查参数
     * @return true:不存在，false：存在
     */
    public static boolean isUnique(Map<String, String> recordMap,
        String checkObj) {
        boolean isExist = recordMap.get(checkObj) != null;
        if (isExist) {
            return false;
        } else {
            recordMap.put(checkObj, checkObj);
            return true;
        }
    }

    /**
     * 替换Message中的参数，获取完整的Message内容
     * @param msgId 带参数的Message
     * @param paramList Message参数
     * @return String 转化后的Message
     */
    public static String getMessage(String msgId, String[] paramList) {
        String msgInfo = msgId;
        for (int i = 0; i < paramList.length; i++) {
            msgInfo = msgInfo.replace("{" + i + "}", paramList[i]);
        }
        return msgInfo;
    }
    
    /**
     * 从左边截取设定长度字符串
     * @param str 被截取字符串
     * @param num 截取长度
     * @return 截取后字符串
     */
    public static String getLeftStr(String str, int num) {
        if (str == null || str.equals("")) { 
            return null; 
        } else {
            String leftStr = str;
            char[] curCharArray = str.toCharArray();
            int ascNumber = 0; 
            int pNum = 0; 
            for (int i = 0; i < curCharArray.length; i++) {
                ascNumber = curCharArray[i];
                if (ascNumber > MAX_ASCII || ascNumber < 0) { 
                    pNum = pNum + ORACLE_CHINESE_BYTE; 
                } else { 
                    pNum = pNum + 1; 
                }
                if (pNum > num) {
                    leftStr = leftStr.substring(0, i);
                    break;
                }
            }
            return leftStr;
        } 
    }
}
