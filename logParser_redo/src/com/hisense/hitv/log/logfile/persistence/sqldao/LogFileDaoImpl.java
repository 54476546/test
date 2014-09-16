package com.hisense.hitv.log.logfile.persistence.sqldao;

import com.hisense.hitv.log.logfile.domain.LogFileVO;
import com.hisense.hitv.log.logfile.persistence.ILogFileDao;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.sql.SQLException;
import java.util.Date;

/**
 * LOGFILE表相关操作实现类
 * @author zhoudi
 * @version 1.0
 */
public class LogFileDaoImpl extends SqlMapClientDaoSupport implements ILogFileDao {
    /**
     * 根据fileName检索记录
     * @param fileName 日志文件名
     * @return logfile对象
     * @throws SQLException DB操作异常
     */
    @Override
	public LogFileVO getLogFileByFileName(String fileName) throws SQLException {
        return (LogFileVO) getSqlMapClientTemplate().queryForObject("getLogFileByFileName", fileName);
    }
    /**
     * logfile表插入操作
     * @param logFileVO logfile参数对象
     * @throws SQLException DB操作异常
     */
    @Override
	public void insertLogFile(LogFileVO logFileVO) throws SQLException {
        getSqlMapClientTemplate().insert("insertLogFile", logFileVO);
    }
    
    /**
     * 获取数据库当前时间
     * @return gmtsysdate
     * @throws SQLException DB操作异常
     */
    @Override
	public Date getCurrentDBTime() throws SQLException {
        return (Date) getSqlMapClientTemplate().queryForObject("getCurrentDBTime");
    }
}