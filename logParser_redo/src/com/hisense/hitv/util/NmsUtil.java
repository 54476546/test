/**
 * 
 */
package com.hisense.hitv.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisense.hitv.nms.javaagent.NEAgentFacade;
import com.hisense.hitv.nms.javaagent.NEConstant;
import com.hisense.hitv.nms.javaagent.NMSSocketListener;
import com.hisense.hitv.nms.javaagent.dto.Alarm;
import com.hisense.hitv.nms.javaagent.dto.Param;
import com.hisense.hitv.service.LogParserService;

/**
 * @author zhoudi
 */
public class NmsUtil {
    
    /**
     * 数据库访问异常
     */
    public static final int DBEXCPTION = 30101822;
    /**
     * 数据库访问异常
     */
    public static final int DB_PARAM_EXCPTION = 30102822;
    /**
     * 日志文件被删除
     */
    public static final int LOG_FILE_NOT_EXIST_EXCPTION = 30202822;
    /**
     * 日志内容未按照规范要求输出相关项目
     */
    public static final int LOG_FILE_FORMAT_EXCPTION = 31010822;
    
    private static final Log LOG = LogFactory.getLog(NmsUtil.class);

    private LogFileConfig logFileConfig;
    private int neId;
    private String neNname;
    private int nePort;
    private String neVersion;
    private String nmsAgentIp;

    /**
     * constructor
     */
    public NmsUtil() {
    }

    /**
     * 构造方法 
     * @param logFileConfig 
     */
    public NmsUtil(LogFileConfig logFileConfig) {
        if (LogParserService.LGTYPE_STV == LogParserService.getCurLGType()) {
            try {
                this.neId = Integer.parseInt(logFileConfig.getProperties("neid"));
                this.neNname = logFileConfig.getProperties("nenname");
                this.nePort = Integer.parseInt(logFileConfig.getProperties("neport"));
                this.neVersion = logFileConfig.getProperties("neversion");
                this.nmsAgentIp = logFileConfig.getProperties("nmsagentip");
                this.setLogFileConfig(logFileConfig);
            } catch (Exception e) {
                LOG.error("NMS Param is error" + e);
            }
        }
    }

    /**
     * @return the neId
     */
    public int getNeId() {
        return neId;
    }

    /**
     * @return the neNname
     */
    public String getNeNname() {
        return neNname;
    }

    /**
     * @return the nePort
     */
    public int getNePort() {
        return nePort;
    }

    /**
     * @return the neVersion
     */
    public String getNeVersion() {
        return neVersion;
    }

    /**
     * @return the nmsAgentIp
     */
    public String getNmsAgentIp() {
        return nmsAgentIp;
    }

    /**
     * 输出告警信息
     * @param errMsg 告警信息
     * @param alarmId 告警ID
     * @param alarmLevel 告警级别
     */
    public void outPutNmsAlarm(String errMsg, int alarmId, int alarmLevel) {
        if (LogParserService.LGTYPE_STV == LogParserService.getCurLGType()) {
            try {
                Alarm alarm = new Alarm();
                alarm.setNeId(this.neId);
                alarm.setAlarmId(alarmId);
                alarm.setAlarmState((short) NEConstant.NEW_ALARM); // 0:产生告警 1：清除告警
                alarm.setAlarmLevel((short) alarmLevel);
                alarm.setAlarmContent(errMsg);
                alarm.setAlarmTime(CalculateDate.today().getTime());
                NEAgentFacade.sendAlarm(alarm, this.nmsAgentIp);
            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

    /**
     * @param logFileConfig the logFileConfig to set
     */
    public void setLogFileConfig(LogFileConfig logFileConfig) {
        this.logFileConfig = logFileConfig;
    }

    /**
     * @return the logFileConfig
     */
    public LogFileConfig getLogFileConfig() {
        return logFileConfig;
    }

    /**
     * 启动NMS
     */
    public void starNms() {
        if (LogParserService.LGTYPE_STV == LogParserService.getCurLGType()) {
            // 获得配置文件全部参数
            try {
                Map<String, String> systemConfigs = logFileConfig.getConfigs();
                NMSSocketListener nmsMonitor = new NMSSocketListener(this.nePort, new AlarmNE());
                nmsMonitor.start();

                Set<String> key = systemConfigs.keySet();
                List<Param> paramList = new ArrayList<Param>();
                Param param = null;
                for (Iterator it = key.iterator(); it.hasNext();) {
                    param = new Param();
                    param.setParamName((String) it.next());
                    param.setParamType("String");
                    param.setParamValue(systemConfigs.get(param.getParamName()));
                    LOG.debug("------paramName:" + param.getParamName() + ", paramType:" + param.getParamType()
                        + ", paramValue:" + param.getParamValue());
                    paramList.add(param);
                }
                LOG.debug("paramList size:" + paramList.size());
                NEAgentFacade.sendConfigParamsWithMaxCount(this.neNname, this.nePort, this.neVersion, paramList,
                    this.nmsAgentIp);
                LOG.info("NMS start success");
            } catch (Exception e) {
                LOG.error("NMS start Exception" + e);
            }
        }
    }
}
