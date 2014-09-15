package com.hisense.hitv.cache;

import com.hisense.hitv.service.LogParserService;
import com.hisense.hitv.subscriber.domain.InternetSubscriberVO;
import com.hisense.hitv.subscriber.persistence.IInternetSubscriberDao;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * InternetSubscriber Cache操作类，提供cache中追加、移除、初期化等功能。
 * @author zhoudi
 * @version 1.0
 */
public class InternetSubscriberCache {
    private static final Log LOG = LogFactory.getLog(InternetSubscriberCache.class);
    // 关键字前缀字符;
    private String keyPrefixSubscriberIDDevCode;

    private GeneralCacheAdministrator cacheAdministrator;
    private IInternetSubscriberDao internetSubscriberDao;

    /**
     * @param keyPrefixSubscriberIDDevCode the keyPrefixSubscriberIDDevCode to
     *        set
     */
    public void setKeyPrefixSubscriberIDDevCode(String keyPrefixSubscriberIDDevCode) {
        this.keyPrefixSubscriberIDDevCode = keyPrefixSubscriberIDDevCode;
    }

    /**
     * @param cacheAdministrator the cacheAdministrator to set
     */
    public void setCacheAdministrator(GeneralCacheAdministrator cacheAdministrator) {
        this.cacheAdministrator = cacheAdministrator;
    }

    /**
     * @param internetSubscriberDao the internetSubscriberDao to set
     */
    public void setInternetSubscriberDao(IInternetSubscriberDao internetSubscriberDao) {
        this.internetSubscriberDao = internetSubscriberDao;
    }

    /**
     * cache中追加SubscriberIdDevCode记录
     * @param key 键值
     * @param value value值
     */
    public void putSubscriberIdDevCode(Object key, Object value) {
        cacheAdministrator.putInCache(this.keyPrefixSubscriberIDDevCode + "_" + key, value);
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
            // NGB系统使用
            try {
                removeAll();
                LOG.info("Internet Subscriber cache");
                List<InternetSubscriberVO> subscribers = internetSubscriberDao.getInternetSubscribers();
                for (InternetSubscriberVO internetSubscriberVO : subscribers) {
                    this.putSubscriberIdDevCode(internetSubscriberVO.getSubscriberId(), internetSubscriberVO
                        .getDeviceCode());
                }
            } catch (DataAccessException e) {
                LOG.error(e);
            }
        }
    }
}
