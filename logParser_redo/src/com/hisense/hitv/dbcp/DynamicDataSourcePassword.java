package com.hisense.hitv.dbcp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.hisense.hitv.cache.ChannelTimeProgramCache;
import com.hisense.hitv.cache.InternetSubscriberCache;
import com.hisense.hitv.cache.ProgramContentCache;
import com.hisense.hitv.cache.ServiceChannelCache;
import com.hisense.hitv.cache.SubscriberCache;
import com.hisense.hitv.cache.TVODContentCache;
import com.hisense.hitv.cache.TVODProgramCache;
import com.hisense.hitv.dbcp.domain.ConnectionItemVO;
import com.hisense.hitv.dbcp.domain.HitvDBUserVO;
import com.hisense.hitv.dbcp.persistence.IHitvDBUserDao;
import com.hisense.hitv.service.LogParserService;
import com.hisense.hitv.EncryptUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 实现Spring InitializingBean接口，采用回调方式 动态设置DataSource和Password。
 * @author zhoudi
 * @version 1.0
 */
public class DynamicDataSourcePassword implements InitializingBean {
    /**
     * 
     */
    private static final String COLON = ":";

    private static Log LOG = LogFactory.getLog(DynamicDataSourcePassword.class);
    
    private static final String RAC = "RAC";
    private static final String DATABASE_IP = "DatabaseIP";
    private static final String DATABASE_PORT = "DatabasePort";
    private static final String SERVICE_NAME = "ServiceName";
    private static final String SID = "SID";
    private static final String ODSRAC = "ODSRAC";
    private static final String ODSUSEFLAG = "ods_use_flag";
    private static final String ODSDATABASE_IP = "ODSDatabaseIP";
    private static final String ODSDATABASE_PORT = "ODSDatabasePort";
    private static final String ODSSERVICE_NAME = "ODSServiceName";
    private static final String ODSSID = "ODSSID";
    private HitvPropertyConfigurer propertyConfigurer;
    private IHitvDBUserDao hitvDBUserSqlDao;
    private BasicDataSource hitvDataSource;
    private BasicDataSource caoprDataSource;
    private BasicDataSource dataSource;
    private BasicDataSource odsDataSource;

    private ProgramContentCache programContentCache;
    private ServiceChannelCache serviceChannelCache;
    private ChannelTimeProgramCache channelTimeProgramCache;
    private TVODProgramCache tvodProgramCache;
    private TVODContentCache tvodContentCache;
    private SubscriberCache subscriberCache;
    private InternetSubscriberCache internetSubscriberCache;

    /**
     * @return the hitvDBUserSqlDao
     */
    public IHitvDBUserDao getHitvDBUserSqlDao() {
        return hitvDBUserSqlDao;
    }

    /**
     * @param hitvDBUserSqlDao the hitvDBUserSqlDao to set
     */
    public void setHitvDBUserSqlDao(IHitvDBUserDao hitvDBUserSqlDao) {
        this.hitvDBUserSqlDao = hitvDBUserSqlDao;
    }

    /**
     * @return the hitvDataSource
     */
    public BasicDataSource getHitvDataSource() {
        return hitvDataSource;
    }

    /**
     * @param hitvDataSource the hitvDataSource to set
     */
    public void setHitvDataSource(BasicDataSource hitvDataSource) {
        this.hitvDataSource = hitvDataSource;
    }
    
    /**
     * @return the odsDataSource
     */
    public BasicDataSource getOdsDataSource() {
        return odsDataSource;
    }

    /**
     * @param odsDataSource the odsDataSource to set
     */
    public void setOdsDataSource(BasicDataSource odsDataSource) {
        this.odsDataSource = odsDataSource;
    }

    /**
     * @return the caoprDataSource
     */
    public BasicDataSource getCaoprDataSource() {
        return caoprDataSource;
    }

    /**
     * @param caoprDataSource the caoprDataSource to set
     */
    public void setCaoprDataSource(BasicDataSource caoprDataSource) {
        this.caoprDataSource = caoprDataSource;
    }

    /**
     * @param programContentCache the programContentCache to set
     */
    public void setProgramContentCache(ProgramContentCache programContentCache) {
        this.programContentCache = programContentCache;
    }
    
    /**
     * @param serviceChannelCache the serviceChannelCache to set
     */
    public void setServiceChannelCache(ServiceChannelCache serviceChannelCache) {
        this.serviceChannelCache = serviceChannelCache;
    }
    
    /**
     * @param channelTimeProgramCache the channelTimeProgramCache to set
     */
    public void setChannelTimeProgramCache(ChannelTimeProgramCache channelTimeProgramCache) {
        this.channelTimeProgramCache = channelTimeProgramCache;
    }
    
    /**
     * @param tvodProgramCache the tvodProgramCache to set
     */
    public void setTvodProgramCache(TVODProgramCache tvodProgramCache) {
        this.tvodProgramCache = tvodProgramCache;
    }

    /**
     * @param tvodContentCache the tvodContentCache to set
     */
    public void setTvodContentCache(TVODContentCache tvodContentCache) {
        this.tvodContentCache = tvodContentCache;
    }

    /**
     * @param subscriberCache the subscriberCache to set
     */
    public void setSubscriberCache(SubscriberCache subscriberCache) {
        this.subscriberCache = subscriberCache;
    }

    /**
     * @param internetSubscriberCache the internetSubscriberCache to set
     */
    public void setInternetSubscriberCache(InternetSubscriberCache internetSubscriberCache) {
        this.internetSubscriberCache = internetSubscriberCache;
    }

    /**
     * @return the propertyConfigurer
     */
    public HitvPropertyConfigurer getPropertyConfigurer() {
        return propertyConfigurer;
    }

    /**
     * @param propertyConfigurer the propertyConfigurer to set
     */
    public void setPropertyConfigurer(HitvPropertyConfigurer propertyConfigurer) {
        this.propertyConfigurer = propertyConfigurer;
    }

    /**
     * 创建Oracle访问URL
     * @param items ConnectionItemVO列表
     * @param serviceName 服务器名
     * @return Oracle访问URL
     */
    private String createRacUrl(List<ConnectionItemVO> items, String serviceName) {
        StringBuffer url = new StringBuffer();
        url.append("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=");

        for (ConnectionItemVO item : items) {
            String address =
                "(ADDRESS = (PROTOCOL = TCP)(HOST = " + item.getHost() + ")(PORT = " + item.getPort() + "))";
            url.append(address);
        }

        url.append(")(LOAD_BALANCE=yes)(CONNECT_DATA = (SERVER = DEDICATED)(SERVICE_NAME = ");
        url.append(serviceName + ")(FAILOVER_MODE = (TYPE = SELECT)(METHOD = BASIC))))");
        return url.toString();
    }

    /**
     * 根据配置文件的属性，构造Oracle集群访问URL。
     * @param pros 配置文件属性
     * @param dataDaseIp 数据库IP
     * @param dataDasePort 数据库端口
     * @param serviceName 数据库服务名
     * @return Oracle集群访问URL
     */
    private String getUrl(Properties pros, String dataDaseIp, 
        String dataDasePort, String serviceName) {
        Enumeration<Object> keys = pros.keys();
        List<ConnectionItemVO> items = new ArrayList<ConnectionItemVO>();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();

            if (key.length() >= dataDaseIp.length()) {
                if (key.substring(0, dataDaseIp.length()).equals(dataDaseIp)) {
                    ConnectionItemVO item = new ConnectionItemVO();

                    item.setHost((String) pros.get(key));
                    String index = key.substring(dataDaseIp.length());
                    item.setPort(pros.getProperty(dataDasePort + index));

                    items.add(item);
                }
            }
        }
        String url = createRacUrl(items, pros.getProperty(serviceName));
        return url;
    }
    
    /**
     * 获取单机Oracle访问URL
     * @param pros 配置文件属性
     * @param dataDaseIp 数据库IP
     * @param dataDasePort 数据库端口
     * @param sid 数据库SID
     * @return Oracle访问URL
     */
    private String getOracleAloneUrl(Properties pros, String dataDaseIp, 
        String dataDasePort, String sid) {
        return "jdbc:oracle:thin:@" + pros.getProperty(dataDaseIp) + COLON 
            + pros.getProperty(dataDasePort) + COLON + pros.getProperty(sid);
    }

    /**
     * InitializingBean接口方法afterPropertiesSet实例化。
     */
    @Override
	public void afterPropertiesSet() {
        try {
            Properties pros = propertyConfigurer.getPros();
            String rac = pros.getProperty(RAC);
            // 没有定义rac的情况下默认为不使用集群
            String url = "";
            if (rac != null && (rac.equalsIgnoreCase("true") || rac.equalsIgnoreCase("yes") || rac.equals("1"))) {
                url = getUrl(pros, DATABASE_IP, DATABASE_PORT, SERVICE_NAME);
            } else {
                url = getOracleAloneUrl(pros, DATABASE_IP, DATABASE_PORT, SID);
            }
            LOG.info("DB connection url:" + url);
            hitvDataSource.setUrl(url);
            dataSource.setUrl(url);

            HitvDBUserVO hitvUserVO = hitvDBUserSqlDao.getHitvDBUserByUser(hitvDataSource.getUsername());
            if (hitvUserVO != null) {
                String pwd = hitvUserVO.getDbPassword();
                hitvDataSource.setPassword(EncryptUtils.AESDecrypt(pwd));
            }
            initODSDataSource();

            if (LogParserService.LGTYPE_NGB40 == LogParserService.getCurLGType()) {
                programContentCache.initCache();
                serviceChannelCache.initCache();
                tvodProgramCache.initCache();
                tvodContentCache.initCache();
                subscriberCache.initCache();
                internetSubscriberCache.initCache();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化ODS库连接
     * @throws IOException 
     */
    private void initODSDataSource() throws IOException {
        Properties pros = propertyConfigurer.getPros();
        if ("1".equals(pros.getProperty(ODSUSEFLAG))) {
            String rac = pros.getProperty(ODSRAC);
            // 没有定义rac的情况下默认为不使用集群
            String url = "";
            if (rac != null && (rac.equalsIgnoreCase("true") || rac.equalsIgnoreCase("yes") || rac.equals("1"))) {
                url = getUrl(pros, ODSDATABASE_IP, ODSDATABASE_PORT, ODSSERVICE_NAME);
            } else {
                url = getOracleAloneUrl(pros, ODSDATABASE_IP, ODSDATABASE_PORT, ODSSID);
            }
            odsDataSource.setUrl(url);
        } else {
            odsDataSource.setUrl(hitvDataSource.getUrl());
            odsDataSource.setUsername(hitvDataSource.getUsername());
            odsDataSource.setPassword(hitvDataSource.getPassword());
        }
        LOG.info("ODSDB connection url:" + odsDataSource.getUrl());
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the dataSource
     */
    public BasicDataSource getDataSource() {
        return dataSource;
    }

}
