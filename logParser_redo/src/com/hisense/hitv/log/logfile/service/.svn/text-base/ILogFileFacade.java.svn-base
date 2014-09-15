package com.hisense.hitv.log.logfile.service;

import com.hisense.hitv.log.logfile.domain.LogFileVO;

import java.sql.SQLException;
import java.util.Date;

/**
 * 文件操作接口
 * @author zhoudi
 * @version 1.0
 */
public interface ILogFileFacade {

    /**
     * logfile表插入操作
     * @param logFileVO logfile参数对象
     * @throws SQLException DB操作异常
     */
    public void insertLogFile(LogFileVO logFileVO) throws SQLException;
    
    /**
     * 日志文件名是否存在验证
     * @param fileName 日志文件名
     * @return true：已存在，false：不存在
     * @throws SQLException DB操作异常
     */
    public boolean checkFileNameExist(String fileName) throws SQLException;
    
    /**
     * 获取数据库当前时间
     * @return gmtsysdate
     * @throws SQLException 
     */
    public Date getCurrentDBTime() throws SQLException;
}