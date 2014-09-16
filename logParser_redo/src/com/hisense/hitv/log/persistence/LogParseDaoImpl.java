package com.hisense.hitv.log.persistence;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * 日志解析相关DB操作接口
 * @author zhoudi
 * @version 1.0
 */
public class LogParseDaoImpl extends SqlMapClientDaoSupport implements
    ILogParseDao {
    private static final Log LOG = LogFactory.getLog(LogParseDaoImpl.class);
    private ILogParseExceptionDao logParseExceptionDao;
    
    /**
     * @return the logParseExceptionDao
     */
    public ILogParseExceptionDao getLogParseExceptionDao() {
        return logParseExceptionDao;
    }

    /**
     * @param logParseExceptionDao the logParseExceptionDao to set
     */
    public void setLogParseExceptionDao(ILogParseExceptionDao logParseExceptionDao) {
        this.logParseExceptionDao = logParseExceptionDao;
    }

    /**
     * 插入操作
     * @param logMap SQL参数及sqlid对象Map
     * @param fileName 文件名
     * @throws SQLException DB操作异常
     */
    @Override
	@SuppressWarnings("unchecked")
    public void insertLogData(Map<String, Object> logMap, String fileName)
        throws SQLException {
        String sql = null;
        int linenum = 0;
        String [] arr =  null;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            getSqlMapClient().startTransaction();
            getSqlMapClient().startBatch();
            for (Entry<String, Object> mapEntry : logMap.entrySet()) {
                arr = mapEntry.getKey().split("/");
                sql = arr[1];
                linenum = Integer.parseInt(arr[0]);
                getSqlMapClient().insert(sql, mapEntry.getValue());
            }
            getSqlMapClient().executeBatch();
            getSqlMapClient().commitTransaction();
        } catch (SQLException e) {
            LOG.error("filename:" + fileName + " line number:" + linenum + " exception:" + e.getMessage());
            getSqlMapClient().commitTransaction();
            paramMap.put("logfilename", fileName);
            paramMap.put("linenumber", linenum);
            paramMap.put("exceptionmessage", e.getMessage());
            try {
                logParseExceptionDao.insertLogSqlException(paramMap);
            } catch (Exception ex) {
                LOG.error("insertLogSqlException:" + ex.getMessage());
            }
        } catch (NumberFormatException e) {
            LOG.error("filename: " + fileName + " DB insert exception:" + e.getMessage());
            getSqlMapClient().commitTransaction();
        } finally {
            getSqlMapClient().endTransaction();
        }
    }
    
    /**
     * 插入操作
     * @param logMap SQL参数Map
     * @param updateSql SQL ID
     * @return 操作记录件数
     * @throws SQLException DB操作异常
     */
    @Override
	public int updateLogData(Map<String, Object> logMap, String updateSql)
        throws SQLException {
        return getSqlMapClientTemplate().update(updateSql, logMap);
    }
    
    /**
     * 获取序列号
     * @return seqid
     * @throws SQLException DB操作异常
     */
    @SuppressWarnings("unchecked")
    public Hashtable<Integer, Integer> getSEQ() throws SQLException {
        return (Hashtable<Integer, Integer>) getSqlMapClientTemplate().queryForObject("getSeq");
    }
    
}
