package com.hisense.hitv.log.util.persistence.sqldao;

import java.sql.SQLException;

import com.hisense.hitv.log.util.persistence.ISystemConfigDao;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * 系统配置相关处理接口
 * @author zhoudi
 * @version 1.0
 */
public class SystemConfigDaoImpl extends SqlMapClientDaoSupport implements ISystemConfigDao {
    
    private static final String GET_SYSTEM_CONFIG_BY_ITEM_NAME = "getSystemConfigByItemName";

    /**
     * 获取系统时间偏移量
     * @return 系统时间偏移量
     * @throws DataAccessException DB操作异常
     */
    public int getServerOffSet() throws DataAccessException {
        String value =
            (String) getSqlMapClientTemplate().queryForObject(GET_SYSTEM_CONFIG_BY_ITEM_NAME, "SystemTimeZone");
        if (value == null) {
            return 0;
        } else {
            return Integer.valueOf(value);
        }
    }

    /**
     * 获取ChannelEventCache检索开始时间偏移量
     * @return 开始时间偏移量
     * @throws DataAccessException DB操作异常
     */
    public int getChannelEventCacheStartOffset() throws DataAccessException {
        String value =
            (String) getSqlMapClientTemplate()
                .queryForObject(GET_SYSTEM_CONFIG_BY_ITEM_NAME, "ChannelEventCacheBefore");
        if (value == null) {
            return 0;
        } else {
            return Integer.valueOf(value);
        }
    }

    /**
     * 获取ChannelEventCache检索结束时间偏移量
     * @return 结束时间偏移量
     * @throws DataAccessException DB操作异常
     */
    public int getChannelEventCacheEndOffset() throws DataAccessException {
        String value =
            (String) getSqlMapClientTemplate().queryForObject(GET_SYSTEM_CONFIG_BY_ITEM_NAME, "ChannelEventCacheAfter");
        if (value == null) {
            return 0;
        } else {
            return Integer.valueOf(value);
        }
    }

    /**
     * 获取Logparser批量提交数据的行数
     * @return Logparser批量提交数据的行数
     * @throws SQLException DB操作异常
     */
    public int getLogparserBatchSize() throws SQLException {
        String value = (String) getSqlMapClient().queryForObject(GET_SYSTEM_CONFIG_BY_ITEM_NAME, "LogparserBatchSize");
        if (value == null) {
            return 0;
        } else {
            return Integer.valueOf(value);
        }
    }
}
