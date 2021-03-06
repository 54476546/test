package com.hisense.hitv.cache;

import com.hisense.hitv.programcontent.domain.ProgramContentVO;
import com.hisense.hitv.programcontent.persistence.IProgramContentDao;
import com.hisense.hitv.service.LogParserService;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * ProgramContent Cache操作类，提供cache中追加、移除、初期化等功能。
 * @author zhoudi
 * @version 1.0
 */
public class ProgramContentCache {
    private static final Log LOG = LogFactory.getLog(ProgramContentCache.class);

    private String keyPrefix; // 关键字前缀字符;

    private GeneralCacheAdministrator cacheAdministrator;
    private IProgramContentDao programContentDao;

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
     * @param programContentDao the programContentDao to set
     */
    public void setProgramContentDao(IProgramContentDao programContentDao) {
        this.programContentDao = programContentDao;
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
                List<ProgramContentVO> contents = programContentDao.getAllVODContent();
                LOG.info("ProgramContent cache");
                for (ProgramContentVO contentVO : contents) {
                    this.put(contentVO.getContentId(), contentVO.getProgramId());
                }
            } catch (DataAccessException e) {
                LOG.error(e);
            }
        }
    }
}
