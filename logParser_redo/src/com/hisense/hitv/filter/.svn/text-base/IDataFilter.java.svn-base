package com.hisense.hitv.filter;

/**
 * 日志记录数据验证接口，验证日志记录格式是否正确。
 * @author zhoudi
 * @version 1.0
 */
public interface IDataFilter {
    /**
     * 日志记录格式验证
     * @param data    待校验的数组
     * @param lineNum 待校验的数据在日志文件中的行数
     * @param index   校验的起始位置  从零开始
     * @param offset  校验的偏移量，index + offset <= length
     * @return true/false
     */
    public boolean isfiltered(String[] data, int lineNum, int index
        , int offset);

    /**
     * 日志记录格式验证
     * @param data    待校验的数组
     * @param lineNum 待校验的数据在日志文件中的行数
     * @return true/false
     */
    public boolean isfiltered(String[] data, int lineNum);
}
