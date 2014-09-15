package com.hisense.hitv.cache;

import com.hisense.hitv.cache.domain.ChannelTimeProgramVO;
import com.hisense.hitv.cache.persistence.IChannelTimeProgramDao;
import com.hisense.hitv.log.util.persistence.ISystemConfigDao;
import com.hisense.hitv.service.LogParserService;
import com.hisense.hitv.util.constant.CachePrefix;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * channeltimeprogram Cache操作类，提供cache中追加、移除、初期化等功能。
 * @author zhoudi
 * @version 1.0
 */
public class ChannelTimeProgramCache {
    private static final Log LOG = LogFactory.getLog(ChannelTimeProgramCache.class);

    private String keyPrefix; // 关键字前缀字符;

    private GeneralCacheAdministrator cacheAdministrator;
    private IChannelTimeProgramDao channelTimeProgramDao;
    private ISystemConfigDao systemConfigSqlDao;

    /**
     * @return the keyPrefix
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * @param keyPrefix the keyPrefix to set
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    /**
     * @param cacheAdministrator the cacheAdministrator to set
     */
    public void setCacheAdministrator(GeneralCacheAdministrator cacheAdministrator) {
        this.cacheAdministrator = cacheAdministrator;
    }

    /**
     * @param channelTimeProgramDao the channelTimeProgramDao to set
     */
    public void setChannelTimeProgramDao(IChannelTimeProgramDao channelTimeProgramDao) {
        this.channelTimeProgramDao = channelTimeProgramDao;
    }

    /**
     * cache中追加记录
     * @param key 键值
     * @param value value值
     */
    public void put(Object key, Object value) {
        cacheAdministrator.putInCache(this.keyPrefix, value);
    }

    /**
     * 移除cache中所有记录
     */
    public void removeAll() {
        cacheAdministrator.flushAll();
    }

    /**
     * 初期化cache
     */
    public void initCache() {
        if (LogParserService.LGTYPE_STV == LogParserService.getCurLGType()) {
            try {
                removeAll();
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("startOffset", systemConfigSqlDao.getChannelEventCacheStartOffset());
                paramMap.put("endOffset", systemConfigSqlDao.getChannelEventCacheEndOffset());
                List<ChannelTimeProgramVO> channelpros = channelTimeProgramDao.getAllChannelTimeProgram(paramMap);
                LOG.debug("ChannelTimeProgram cache. " + channelpros.size());
                this.put(CachePrefix.CHANNEL_TIME_PROGRAM_PREFIX, channelpros);  
                LOG.debug("ChannelTimeProgram cache end.");
            } catch (DataAccessException e) {
                LOG.error(e);
            }
        }
    }

    /**
     * @param systemConfigSqlDao the systemConfigSqlDao to set
     */
    public void setSystemConfigSqlDao(ISystemConfigDao systemConfigSqlDao) {
        this.systemConfigSqlDao = systemConfigSqlDao;
    }
}
