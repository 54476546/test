package com.hisense.hitv.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 实现日志记录数据验证接口，验证日志记录格式是否正确。
 * @author zhoudi
 * @version 1.0
 */
public class DataFilterImpl implements IDataFilter {

    private static final Log LOG = LogFactory.getLog(DataFilterImpl.class);

    /**
     * 日志记录格式验证
     * @param data    待校验的数组
     * @param lineNum 待校验的数据在日志文件中的行数
     * @param index   校验的起始位置  从零开始
     * @param offset  校验的偏移量，index + offset <= length
     * @return true/false
     */
    public boolean isfiltered(String[] data, int lineNum, int index
        , int offset) {
        boolean flag = false;
        int start = (index < data.length ? index : data.length);
        int length = ((index + offset) < data.length ? offset : data.length);

        int j = 0;
        for (int i = start; i < length; i++) {
//            if (data[i].trim().isEmpty() || "null".equals(data[i])) {
            if ("null".equals(data[i])) {
                LOG.error("Log line:" + (lineNum) + " column: " + (i + 1)
                    + "is error");
                flag = true;
                j++;
                break;
            }
        }
        return flag;
    }

    /**
     * 日志记录格式验证
     * @param data    待校验的数组
     * @param lineNum 待校验的数据在日志文件中的行数
     * @return true/false
     */
    public boolean isfiltered(String[] data, int lineNum) {
        return isfiltered(data, lineNum, 0, data.length);
    }
}
