package com.hisense.hitv.log.util.persistence;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

/**
 * 系统配置相关处理接口
 * @author zhoudi
 * @version 1.0
 */
public interface ISystemConfigDao {

    /**
     * 获取系统时间偏移量
     * @return 系统时间偏移量
     * @throws DataAccessException DB操作异常
     */
    public int getServerOffSet() throws DataAccessException;
    /**
     * 获取ChannelEventCache检索开始时间偏移量
     * @return 开始时间偏移量
     * @throws DataAccessException DB操作异常
     */
    public int getChannelEventCacheStartOffset() throws DataAccessException;
    /**
     * 获取ChannelEventCache检索结束时间偏移量
     * @return 结束时间偏移量
     * @throws DataAccessException DB操作异常
     */
    public int getChannelEventCacheEndOffset() throws DataAccessException;
    /**
     * 获取Logparser批量提交数据的行数
     * @return Logparser批量提交数据的行数
     * @throws SQLException DB操作异常
     */
    public int getLogparserBatchSize() throws SQLException;
}