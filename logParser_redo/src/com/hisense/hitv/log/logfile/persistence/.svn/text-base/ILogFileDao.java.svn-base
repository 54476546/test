package com.hisense.hitv.log.logfile.persistence;

import com.hisense.hitv.log.logfile.domain.LogFileVO;

import java.sql.SQLException;
import java.util.Date;

/**
 * LOGFILE表相关操作接口
 * @author zhoudi
 * @version 1.0
 */
public interface ILogFileDao {
    /**
     * 根据fileName检索记录
     * @param fileName 日志文件名
     * @return logfile对象
     * @throws SQLException DB操作异常
     */
    public LogFileVO getLogFileByFileName(String fileName) throws SQLException;
    
    /**
     * logfile表插入操作
     * @param logFileVO logfile参数对象
     * @throws SQLException DB操作异常
     */
    public void insertLogFile(LogFileVO logFileVO) throws SQLException;
    
    /**
     * 获取数据库当前时间
     * @return gmtsysdate
     * @throws SQLException DB操作异常
     */
    public Date getCurrentDBTime() throws SQLException;
}