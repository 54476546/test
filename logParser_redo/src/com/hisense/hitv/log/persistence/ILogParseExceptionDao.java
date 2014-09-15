package com.hisense.hitv.log.persistence;

import java.sql.SQLException;
import java.util.Map;

/**
 * 日志解析记录异常信息接口
 * @author tianyuqi
 * @version 1.0
 */
public interface ILogParseExceptionDao {
    /**
     * 插入LogParserSqlException表操作
     * @param paramMap 参数列表
     * @throws SQLException DB操作异常
     */
    public void insertLogSqlException(Map<String, Object> paramMap) throws SQLException;
}
