package com.hisense.hitv.parse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.hisense.hitv.cache.domain.ChannelTimeProgramVO;
import com.hisense.hitv.cache.persistence.IChannelTimeProgramDao;
import com.hisense.hitv.compress.ICompressLogFile;
import com.hisense.hitv.filter.IDataFilter;
import com.hisense.hitv.log.persistence.ILogParseDao;
import com.hisense.hitv.log.util.persistence.ISystemConfigDao;
import com.hisense.hitv.programcontent.domain.ProgramContentVO;
import com.hisense.hitv.programcontent.domain.ServiceChannelVO;
import com.hisense.hitv.programcontent.domain.TVODContentVO;
import com.hisense.hitv.programcontent.domain.TVODProgramVO;
import com.hisense.hitv.programcontent.persistence.IProgramContentDao;
import com.hisense.hitv.programcontent.persistence.IServiceChannelDao;
import com.hisense.hitv.programcontent.persistence.ITVODContentDao;
import com.hisense.hitv.programcontent.persistence.ITVODProgramDao;
import com.hisense.hitv.resource.domain.LogXmlNetypeVO;
import com.hisense.hitv.resource.exception.CacheException;
import com.hisense.hitv.subscriber.domain.InternetSubscriberVO;
import com.hisense.hitv.subscriber.domain.SubscriberVO;
import com.hisense.hitv.subscriber.persistence.IInternetSubscriberDao;
import com.hisense.hitv.subscriber.persistence.ISubscriberDao;
import com.hisense.hitv.util.CommonUtil;
import com.hisense.hitv.util.LogFileConfig;
import com.hisense.hitv.util.constant.CachePrefix;
import com.hisense.hitv.util.constant.Message;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * 解析器抽象类
 * @author zhoudi
 * @version 1.0
 */
public abstract class AbstractParser implements IParser {
   
    /**
     * 逗号
     */
    public static final String COMMA = ",";
    /**
     * 线程休眠时间ms
     */
    public static final int SLEEP = 1000 * 30;
    /**
     * 文件列表过大时，线程休眠时间ms
     */
    public static final int FILElIST_THREAD_SLEEP = 500;
    /**
     * UTF-8 
     */
    public static final String CODE_FORMAT_UTF_8 = "UTF-8";

    /**
     * cache中数据检索失败场合
     */
    private static final long LONG_ERR_CACHE_VALUE = -1L;

    /**
     * cache中数据检索失败场合
     */
    private static final String STRING_ERR_CACHE_VALUE = "";

    /**
     * 下划线
     */
    private static final String UNDERLINE = "_";

    private static Log LOG = LogFactory.getLog(AbstractParser.class);

    /**
     * 日志记录数据验证对象
     */
    protected IDataFilter stringDataFilter;
    /**
     * 文件压缩处理对象
     */
    protected ICompressLogFile compressLogFile;
    /**
     * 日志文件操作对象
     */
    protected LogFileConfig logFileConfig;
    /**
     * ProgramContent检索对象
     */
    protected IProgramContentDao programContentDao;
    /**
     * ServiceChannel检索对象
     */
    protected IServiceChannelDao serviceChannelDao;
    /**
     * ChannelTimeProgram检索对象
     */
    protected IChannelTimeProgramDao channelTimeProgramDao;
    /**
     * TVODProgram检索对象
     */
    protected ITVODProgramDao tvodProgramDao;
    /**
     * TVODContent检索对象
     */
    protected ITVODContentDao tvodContentDao;
    /**
     * Subscriber检索对象
     */
    protected ISubscriberDao subscriberDao;
    /**
     * InternetSubscriber检索对象
     */
    protected IInternetSubscriberDao internetSubscriberDao;
    /**
     * 系统配置相关处理对象
     */
    protected ISystemConfigDao systemConfigDao;
    /**
     * hitvDB操作对象
     */
    protected ILogParseDao hitvLogParseSqlDao;
    /**
     * caoprDB操作对象
     */
    protected ILogParseDao caoprLogParseSqlDao;
    /**
     * 网元结构对象
     */
    protected LogXmlNetypeVO netypeVO;
    /**
     * 网元日志类型
     */
    protected String fileCategory;


    /**
     * cache操作对象
     */
    private GeneralCacheAdministrator cacheAdministrator;

    /**
     * 日志解析
     */
    public abstract void processLogFile();

    /**
     * @return the stringDataFilter
     */
    public IDataFilter getStringDataFilter() {
        return stringDataFilter;
    }

    /**
     * @param stringDataFilter the stringDataFilter to set
     */
    public void setStringDataFilter(IDataFilter stringDataFilter) {
        this.stringDataFilter = stringDataFilter;
    }

    /**
     * @return the logFileConfig
     */
    public LogFileConfig getLogFileConfig() {
        return logFileConfig;
    }

    /**
     * @param logFileConfig the logFileConfig to set
     */
    public void setLogFileConfig(LogFileConfig logFileConfig) {
        this.logFileConfig = logFileConfig;
    }

    /**
     * @param compressLogFile the compressLogFile to set
     */
    public void setCompressLogFile(ICompressLogFile compressLogFile) {
        this.compressLogFile = compressLogFile;
    }

    /**
     * @param programContentDao the programContentDao to set
     */
    public void setProgramContentDao(IProgramContentDao programContentDao) {
        this.programContentDao = programContentDao;
    }

    /**
     * @param serviceChannelDao the serviceChannelDao to set
     */
    public void setServiceChannelDao(IServiceChannelDao serviceChannelDao) {
        this.serviceChannelDao = serviceChannelDao;
    }

    /**
     * @param channelTimeProgramDao the channelTimeProgramDao to set
     */
    public void setChannelTimeProgramDao(IChannelTimeProgramDao channelTimeProgramDao) {
        this.channelTimeProgramDao = channelTimeProgramDao;
    }

    /**
     * @param tvodProgramDao the tvodProgramDao to set
     */
    public void setTvodProgramDao(ITVODProgramDao tvodProgramDao) {
        this.tvodProgramDao = tvodProgramDao;
    }

    /**
     * @param tvodContentDao the tvodContentDao to set
     */
    public void setTvodContentDao(ITVODContentDao tvodContentDao) {
        this.tvodContentDao = tvodContentDao;
    }

    /**
     * @param subscriberDao the subscriberDao to set
     */
    public void setSubscriberDao(ISubscriberDao subscriberDao) {
        this.subscriberDao = subscriberDao;
    }

    /**
     * @param internetSubscriberDao the internetSubscriberDao to set
     */
    public void setInternetSubscriberDao(IInternetSubscriberDao internetSubscriberDao) {
        this.internetSubscriberDao = internetSubscriberDao;
    }

    /**
     * @param systemConfigDao the systemConfigDao to set
     */
    public void setSystemConfigDao(ISystemConfigDao systemConfigDao) {
        this.systemConfigDao = systemConfigDao;
    }

    /**
     * @param cacheAdministrator the cacheAdministrator to set
     */
    public void setCacheAdministrator(GeneralCacheAdministrator cacheAdministrator) {
        this.cacheAdministrator = cacheAdministrator;
    }

    /**
     * @param caoprLogParseSqlDao the caoprLogParseSqlDao to set
     */
    public void setCaoprLogParseSqlDao(ILogParseDao caoprLogParseSqlDao) {
        this.caoprLogParseSqlDao = caoprLogParseSqlDao;
    }

    /**
     * @param hitvLogParseSqlDao the hitvLogParseSqlDao to set
     */
    public void setHitvLogParseSqlDao(ILogParseDao hitvLogParseSqlDao) {
        this.hitvLogParseSqlDao = hitvLogParseSqlDao;
    }

    /**
     * @param netypeVO the netypeVO to set
     */
    public void setNetypeVO(LogXmlNetypeVO netypeVO) {
        this.netypeVO = netypeVO;
    }

    /**
     * the fileCategory to set
     */
    public void setFileCategory() {
        this.fileCategory = netypeVO.getNeTypeId();
    }

    /**
     * @return the fileCategory
     */
    public String getFileCategory() {
        return this.fileCategory;
    }

    /**
     * 在cache中根据key取得对应的数值
     * @param keyPrefix 前缀
     * @param key 参照数值
     * @return 被检索的数值
     * @throws CacheException cache异常
     */
    public Object getCacheValueByKey(String keyPrefix, Object key) throws CacheException {
        String cacheKey = keyPrefix + UNDERLINE + key;
        Object value = null;
        LOG.debug("key is " + cacheKey);
        if (CachePrefix.CHANNEL_TIME_PROGRAM_PREFIX.equals(keyPrefix)) {
            value = getProgramNameByChannelIdAndStartTime(key, keyPrefix);
        } else {
            try {
                value = cacheAdministrator.getFromCache(cacheKey);
            } catch (NeedsRefreshException nre) {
                // Cache中没有则从库获得数据.
                if (CachePrefix.VOD_PREFIX.equals(keyPrefix)) {
                    value = getVodByContentId(key, cacheKey);
                } else if (CachePrefix.TVOD_PROGRAM_PREFIX.equals(keyPrefix)) {
                    value = getTvodByProgramId(key, cacheKey);
                } else if (CachePrefix.TVOD_CONTENT_PREFIX.equals(keyPrefix)) {
                    value = getTvodByCotentId(key, cacheKey);
                } else if (CachePrefix.SUSBCRIBER_DEVICEID_PREFIX.equals(keyPrefix)
                    || CachePrefix.SUSBCRIBER_MAC_PREFIX.equals(keyPrefix)
                    || CachePrefix.SUBSCRIBER_VSPID_PREFIX.equals(keyPrefix)) {
                    value = getSubscriberById(key, cacheKey, keyPrefix);
                } else if (CachePrefix.SUSBCRIBER_DEVICECODE_PREFIX.equals(keyPrefix)) {
                    value = getInternetSubscriberById(key, cacheKey);
                } else if (CachePrefix.MAC_SUSBCRIBER_PREFIX.equals(keyPrefix)) {
                    value = getSubscriberByMac(key, cacheKey);
                } else if (CachePrefix.SERVICE_CHANNEL_PREFIX.equals(keyPrefix)) {
                    value = getChannelIdByServiceId(key, cacheKey);
                }
            }
        }
        LOG.debug("value is " + value);
        return value;
    }

    /**
     * 根据Mac获取subscriberId
     * @param key Mac
     * @param cacheKey cache中存放的key
     * @return subscriberId
     * @throws CacheException
     */
    private Object getSubscriberByMac(Object key, String cacheKey) throws CacheException {
        long value = LONG_ERR_CACHE_VALUE;
        String errMsg = CommonUtil.getMessage(Message.CACHE_ERR_INFO, new String[] {"Mac " + key.toString()});
        CacheException cacheE = new CacheException("", errMsg);
        try {
            SubscriberVO contentVO = subscriberDao.getSubscriberByMac(key.toString());
            if (contentVO != null) {
                value = contentVO.getSubscriberId();
                cacheAdministrator.putInCache(cacheKey, value);
            } else {
                // 当前content不存在，取消对myKey的更新
                cacheAdministrator.cancelUpdate(cacheKey);
                throw cacheE;
            }
        } catch (DataAccessException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        }
        return value;
    }

    /**
     * 根据subscriberId获取DeviceCode
     * @param key subscriberId
     * @param cacheKey cache中存放的key
     * @return DeviceCode
     */
    private Object getInternetSubscriberById(Object key, String cacheKey) {
        Object value = STRING_ERR_CACHE_VALUE;
        try {
            InternetSubscriberVO contentVO =
                internetSubscriberDao.getInternetSubscriberById(Long.parseLong(key.toString()));
            if (contentVO != null) {
                value = contentVO.getDeviceCode();
                cacheAdministrator.putInCache(cacheKey, value);
            } else {
                // 当前content不存在，取消对myKey的更新
                cacheAdministrator.cancelUpdate(cacheKey);
            }
        } catch (DataAccessException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
        } catch (NumberFormatException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
        }
        return value;
    }

    /**
     * 根据subscriberId获取DeviceId、Mac、Administrator
     * @param key subscriberId
     * @param cacheKey cache检索key
     * @param keyPrefix cache前缀
     * @return DeviceId、Mac、Administrator
     */
    private Object getSubscriberById(Object key, String cacheKey, String keyPrefix) {
        Object value = STRING_ERR_CACHE_VALUE;
        try {
            SubscriberVO contentVO = subscriberDao.getSubscriberById(Long.parseLong(key.toString()));
            if (contentVO != null) {
                if (CachePrefix.SUSBCRIBER_DEVICEID_PREFIX.equals(keyPrefix)) {
                    value = contentVO.getDeviceId();
                } else if (CachePrefix.SUSBCRIBER_MAC_PREFIX.equals(keyPrefix)) {
                    value = contentVO.getMac();
                } else if (CachePrefix.SUBSCRIBER_VSPID_PREFIX.equals(keyPrefix)) {
                    value = contentVO.getAdministrator();
                }
                cacheAdministrator.putInCache(cacheKey, value);
            } else { // 当前content不存在，取消对myKey的更新
                cacheAdministrator.cancelUpdate(cacheKey);
            }
        } catch (DataAccessException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
        } catch (NumberFormatException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
        }
        return value;
    }

    /**
     * 根据CotentId获取ProgramId
     * @param key CotentId
     * @param cacheKey cache检索key
     * @return ProgramId
     * @throws CacheException
     */
    private Object getTvodByCotentId(Object key, String cacheKey) throws CacheException {
        long value = LONG_ERR_CACHE_VALUE;
        String errMsg = CommonUtil.getMessage(Message.CACHE_ERR_INFO, new String[] {"CotentId " + key.toString()});
        CacheException cacheE = new CacheException("", errMsg);
        try {
            TVODContentVO contentVO = tvodContentDao.getTVODByContentId(Long.parseLong(key.toString()));
            if (contentVO != null) {
                value = contentVO.getProgramId();
                cacheAdministrator.putInCache(cacheKey, value);
            } else {
                // 当前content不存在，取消对myKey的更新
                cacheAdministrator.cancelUpdate(cacheKey);
                throw cacheE;
            }
        } catch (DataAccessException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        } catch (NumberFormatException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        }
        return value;
    }

    /**
     * 根据ProgramId获取ChannelId
     * @param key ProgramId
     * @param cacheKey cache检索key
     * @return ChannelId
     * @throws CacheException
     */
    private Object getTvodByProgramId(Object key, String cacheKey) throws CacheException {
        long value = LONG_ERR_CACHE_VALUE;
        String errMsg = CommonUtil.getMessage(Message.CACHE_ERR_INFO, new String[] {"ProgramId " + key.toString()});
        CacheException cacheE = new CacheException("", errMsg);
        try {
            TVODProgramVO contentVO = tvodProgramDao.getTVODProgramById(Long.parseLong(key.toString()));
            if (contentVO != null) {
                value = contentVO.getChannelId();
                cacheAdministrator.putInCache(cacheKey, value);
            } else {
                // 当前content不存在，取消对myKey的更新
                cacheAdministrator.cancelUpdate(cacheKey);
                throw cacheE;
            }
        } catch (DataAccessException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        } catch (NumberFormatException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        }
        return value;
    }

    /**
     * 根据ContentById获取ProgramId
     * @param key ContentById
     * @param cacheKey cache检索key
     * @return ProgramId
     * @throws CacheException
     */
    private Object getVodByContentId(Object key, String cacheKey) throws CacheException {
        long value = LONG_ERR_CACHE_VALUE;
        String errMsg = CommonUtil.getMessage(Message.CACHE_ERR_INFO, new String[] {"ContentId " + key.toString()});
        CacheException cacheE = new CacheException("", errMsg);
        try {
            ProgramContentVO contentVO = programContentDao.getVODContentById(Long.parseLong(key.toString()));
            if (contentVO != null) {
                value = contentVO.getProgramId();
                cacheAdministrator.putInCache(cacheKey, value);
            } else {
                // 当前content不存在，取消对myKey的更新
                cacheAdministrator.cancelUpdate(cacheKey);
                throw cacheE;
            }
        } catch (DataAccessException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        } catch (NumberFormatException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        }
        return value;
    }

    /**
     * 根据ServiceId获取ChannelId
     * @param key ServiceId
     * @param cacheKey cache检索key
     * @return ChannelId
     * @throws CacheException
     */
    private Object getChannelIdByServiceId(Object key, String cacheKey) throws CacheException {
        long value = LONG_ERR_CACHE_VALUE;
        String errMsg = CommonUtil.getMessage(Message.CACHE_ERR_INFO, new String[] {"ServiceId " + key.toString()});
        CacheException cacheE = new CacheException("", errMsg);
        try {
            ServiceChannelVO channelVO = serviceChannelDao.getServiceChannelById(Long.parseLong(key.toString()));
            if (channelVO != null) {
                value = channelVO.getChannelId();
                cacheAdministrator.putInCache(cacheKey, value);
            } else {
                // 当前content不存在，取消对myKey的更新
                cacheAdministrator.cancelUpdate(cacheKey);
                throw cacheE;
            }
        } catch (SQLException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        } catch (NumberFormatException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        }
        return value;
    }

    /**
     * 根据ChannelId、StartTime获取ProgramName
     * @param key ChannelId、StartTime
     * @param cacheKey cache检索key
     * @return ProgramCode,ProgramName
     * @throws CacheException
     */
    @SuppressWarnings("unchecked")
    private Object getProgramNameByChannelIdAndStartTime(Object key, String cacheKey) throws CacheException {
        String value = STRING_ERR_CACHE_VALUE;
        String errMsg = CommonUtil.getMessage(Message.CACHE_ERR_INFO
                , new String[] {"ChannelId,StartTime: " + key.toString()});
        CacheException cacheE = new CacheException("", errMsg);
        ChannelTimeProgramVO keyVO = new ChannelTimeProgramVO();
        String[] keyArr = key.toString().split(COMMA);
        List<ChannelTimeProgramVO> channelpros = new ArrayList<ChannelTimeProgramVO>();
        try {
            keyVO.setChannelId(Long.parseLong(keyArr[0]));
            keyVO.setStartTime(Long.parseLong(keyArr[1]));
            channelpros = (List<ChannelTimeProgramVO>) cacheAdministrator.getFromCache(cacheKey);
            // 根据channelid，time遍历channelevent查找对应的program
            for (ChannelTimeProgramVO listEntry : channelpros) {
                // 点播时间>=开始时间的第一行记录
                if (listEntry.getChannelId() == keyVO.getChannelId() && listEntry.getStartTime() <= keyVO.getStartTime()
                    && listEntry.getEndTime() >  keyVO.getStartTime()) {
                    value = URLEncoder.encode(listEntry.getProgramCode(), CODE_FORMAT_UTF_8) + COMMA  
                            + URLEncoder.encode(listEntry.getProgramName(), CODE_FORMAT_UTF_8) + COMMA 
                            + URLEncoder.encode(String.valueOf(listEntry.getEventId()), CODE_FORMAT_UTF_8);
                    break;
                }
            }
            // 查找失败
            if (STRING_ERR_CACHE_VALUE.equals(value)) {
                throw new NeedsRefreshException(channelpros);
            }
        } catch (NeedsRefreshException nre) {
            try {
                ChannelTimeProgramVO channelProVO = channelTimeProgramDao.getProgramByChannelTime(keyVO);
                if (channelProVO != null) {
                    value = URLEncoder.encode(channelProVO.getProgramCode(), CODE_FORMAT_UTF_8) + COMMA  
                            + URLEncoder.encode(channelProVO.getProgramName(), CODE_FORMAT_UTF_8) + COMMA  
                            + URLEncoder.encode(String.valueOf(channelProVO.getEventId()), CODE_FORMAT_UTF_8);
                    channelpros.add(channelProVO);
                    cacheAdministrator.putInCache(cacheKey, channelpros);
                } else {
                    // 当前content不存在，取消对myKey的更新
                    cacheAdministrator.cancelUpdate(cacheKey);
                    throw cacheE;
                }
            } catch (DataAccessException ex) {
                cacheAdministrator.cancelUpdate(cacheKey);
                throw cacheE;
            } catch (UnsupportedEncodingException e) {
                cacheAdministrator.cancelUpdate(cacheKey);
                throw cacheE;
            }
        } catch (NumberFormatException ex) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        } catch (UnsupportedEncodingException e) {
            cacheAdministrator.cancelUpdate(cacheKey);
            throw cacheE;
        }
        return value;
    }

}
