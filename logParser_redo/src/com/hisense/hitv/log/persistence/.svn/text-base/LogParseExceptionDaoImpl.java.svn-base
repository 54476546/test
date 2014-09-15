package com.hisense.hitv.log.persistence;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
/**
 * 日志解析记录异常信息接口实现类
 * @author tianyuqi
 */
public class LogParseExceptionDaoImpl extends SqlMapClientDaoSupport 
    implements ILogParseExceptionDao {

    @Override
    public void insertLogSqlException(Map<String, Object> paramMap)
        throws SQLException {
        getSqlMapClientTemplate().insert("insertLogParserSqlException", paramMap);
    }

}
