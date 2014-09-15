package com.hisense.hitv.cache;

import com.hisense.hitv.programcontent.domain.ServiceChannelVO;
import com.hisense.hitv.programcontent.persistence.IServiceChannelDao;
import com.hisense.hitv.service.LogParserService;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * ServiceChannel Cache操作类，提供cache中追加、移除、初期化等功能。
 * @author zhoudi
 * @version 1.0
 */
public class ServiceChannelCache {
    private static final Log LOG = LogFactory.getLog(ServiceChannelCache.class);

    private String keyPrefix; // 关键字前缀字符;

    private GeneralCacheAdministrator cacheAdministrator;
    private IServiceChannelDao serviceChannelDao;

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
     * @param serviceChannelDao the serviceChannelDao to set
     */
    public void setServiceChannelDao(IServiceChannelDao serviceChannelDao) {
        this.serviceChannelDao = serviceChannelDao;
    }

    /**
     * cache中追加记录
     * @param key 键值
     * @param value value值
     */
    public void put(Object key, Object value) {
        cacheAdministrator.putInCache(this.keyPrefix + "_" + key, value);
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
        if (LogParserService.getCurLGType() == LogParserService.LGTYPE_NGB40) { 
            try {
                removeAll();
                List<ServiceChannelVO> channels = serviceChannelDao.getAllServiceChannel();
                LOG.info("service channel cache.");
                for (ServiceChannelVO channelVO : channels) {
                    this.put(channelVO.getServiceId(), channelVO.getChannelId());
                }
            } catch (SQLException e) {
                LOG.error(e);
            }
        }
    }
}
