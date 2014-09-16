package com.hisense.hitv.log.logfile.service;

import com.hisense.hitv.log.logfile.domain.LogFileVO;
import com.hisense.hitv.log.logfile.persistence.ILogFileDao;

import java.sql.SQLException;
import java.util.Date;

/**
 * 文件操作实现类
 * @author zhoudi
 * @version 1.0
 */
public class LogFileFacadeImpl implements ILogFileFacade {

    private ILogFileDao logFileDao;
    
    /**
     * @return the logFileDao
     */
    public ILogFileDao getLogFileDao() {
        return logFileDao;
    }
    /**
     * @param logFileDao the logFileDao to set
     */
    public void setLogFileDao(ILogFileDao logFileDao) {
        this.logFileDao = logFileDao;
    }
    /**
     * logfile表插入操作
     * @param logFileVO logfile参数对象
     * @throws SQLException DB操作异常
     */
    @Override
	public void insertLogFile(LogFileVO logFileVO) throws SQLException {
        logFileDao.insertLogFile(logFileVO);
    }
    /**
     * 日志文件名是否存在验证
     * @param fileName 日志文件名
     * @return true：已存在，false：不存在
     * @throws SQLException DB操作异常
     */
    @Override
	public boolean checkFileNameExist(String fileName) throws SQLException {
        LogFileVO exist = logFileDao.getLogFileByFileName(fileName);
        if (exist != null) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 获取数据库当前时间
     * @return gmtsysdate
     * @throws SQLException 
     */
    @Override
	public Date getCurrentDBTime() throws SQLException {
        return logFileDao.getCurrentDBTime();
    }
}