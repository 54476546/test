package com.hisense.hitv.log.persistence;

import java.sql.SQLException;
import java.util.Map;

/**
 * 日志解析相关DB操作接口
 * @author zhoudi
 * @version 1.0
 */
public interface ILogParseDao {
    /**
     * 插入操作
     * @param logMap SQL参数及sqlid对象Map
     * @param fileName 文件名
     * @throws SQLException DB操作异常
     */
    public void insertLogData(Map<String, Object> logMap, String fileName) throws SQLException;
 
    /**
     * 插入操作
     * @param logMap SQL参数Map
     * @param updateSql SQL ID
     * @return 操作记录件数
     * @throws SQLException DB操作异常
     */
    public int updateLogData(Map<String, Object> logMap, String updateSql) throws SQLException;
}
