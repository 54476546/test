package com.hisense.hitv.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.regex.MalformedPatternException;
import org.springframework.jdbc.UncategorizedSQLException;

import com.googlecode.asyn4j.service.AsynService;
import com.hisense.hitv.log.logfile.domain.LogFileVO;
import com.hisense.hitv.log.logfile.service.ILogFileFacade;
import com.hisense.hitv.nms.javaagent.NEConstant;
import com.hisense.hitv.parse.common.Formattor;
import com.hisense.hitv.parse.common.Validator;
import com.hisense.hitv.resource.constant.LogXmlName;
import com.hisense.hitv.resource.domain.LogXmlActionItemVO;
import com.hisense.hitv.resource.domain.LogXmlCheckItemVO;
import com.hisense.hitv.resource.domain.LogXmlConvertVO;
import com.hisense.hitv.resource.domain.LogXmlLogVO;
import com.hisense.hitv.resource.domain.LogXmlNetypeVO;
import com.hisense.hitv.resource.domain.LogXmlPropertyItemVO;
import com.hisense.hitv.resource.domain.LogXmlValidationVO;
import com.hisense.hitv.resource.exception.CacheException;
import com.hisense.hitv.util.AlarmDef;
import com.hisense.hitv.util.CalculateDate;
import com.hisense.hitv.util.CommonUtil;
import com.hisense.hitv.util.LogFileName;
import com.hisense.hitv.util.NmsUtil;
import com.hisense.hitv.util.SpringUtil;
import com.hisense.hitv.util.constant.Message;

/**
 * 日志文件解析处理，读取日志文件，再根据日志配置格式逐行解析、 将数据保存至对应的数据库表中。如果某一行日志记录解析失败，
 * 则继续处理后续记录。整个日志文件解析完成后生成备份文件并 删除源 文件，异常场合，生成异常备份文件。
 * @author zhoudi
 * @version 1.0
 */
public class LogFileParser extends AbstractParser {

    /**
     * 
     */
    private static final String SOLIDUS = "/";

    /**
     * 冒号
     */
    private static final String COLON = ":";
    
    /**
     * 数组下标3
     */
    private static final int ARRAY_INDEX_3 = 3;

    /**
     * 数组大小4
     */
    private static final int ARRAY_COUNT_4 = 4;
    /**
     * 数组大小3
     */
    private static final int ARRAY_COUNT_3 = 3;
    /**
     * 初始化失败
     */
    private static final int INIT_FAIL = 0;
    /**
     * 初始化成功
     */
    private static final int INIT_SUCCESS = 1;
    /**
     * 无效数据
     */
    private static final int INIT_INVALID = 2;
    private static final String LINENUM = "lineNum";
    private static final String ERRORNUM = "errorNum";

    private static final Log LOG = LogFactory.getLog(LogFileParser.class);
    private NmsUtil nmsUtil;
    
    private ILogFileFacade logFileFacade;
    private Semaphore semp;
    private Vector<String> processingFileList = new Vector<String>();

    /**
     * @param netypeVO the LogXmlNetypeVO to set
     */
    @Override
	public void setNetype(LogXmlNetypeVO netypeVO) {
        this.netypeVO = netypeVO;
    }

    /**
     * @param logFileFacade the LogFileFacade to set
     */
    public void setLogFileFacade(ILogFileFacade logFileFacade) {
        this.logFileFacade = logFileFacade;
    }

    /**
     * 线程启动
     */
    @Override
	public void run() {
        Thread.currentThread().setName(netypeVO.getNeTypeId());
        this.processLogFile();
    }

    /**
     * 解析，导入数据库，备份，删除
     */
    @Override
	public void processLogFile() {
        // 初始化异步工作服务    
        AsynService asynService = (AsynService) SpringUtil.getBean("asynService");
        nmsUtil = new NmsUtil(this.logFileConfig);
        /* Get log file name */
        LogFileName fileNames = new LogFileName();
        this.setFileCategory();
        int threadNum = Integer.valueOf(logFileConfig.getProperties(
            (fileCategory + "_thread_num").toLowerCase()));
        semp = new Semaphore(threadNum);
        String fileDir = logFileConfig.getLogFileFolderDir(fileCategory);
        LOG.debug("LogFileParser:" + fileCategory);
        try {
            while (true) {
                List<String> files = fileNames.getFileName(fileDir, fileCategory, logFileConfig.getFileNameExtension());

                while (files.size() != 0) {
                    String fileName = files.get(0);
                    if (processingFileList.contains(fileName)) {
                        files.remove(0);
                    } else if (semp.tryAcquire()) {
                        processingFileList.add(fileName);
                        asynService.addWork(this, "parseLogFile",
                            new Object[] {fileDir, fileName});
                        files.remove(0);
                    } else {
                        Thread.sleep(FILElIST_THREAD_SLEEP);
                    }
                }
                Thread.sleep(SLEEP);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }
    
    /**
     * @param fileDir 
     * @param fileName 
     */
    public void parseLogFile(String fileDir, String fileName) {
        // 文件备份目录
        String fileBackDir = logFileConfig.getBackUpFileFolderDir(fileCategory);
        String exceptionFileBackDir = logFileConfig.getErrorBackUpFileFolderDir(fileCategory);
        String redoFileBackupPath = logFileConfig.getRedoBackupFileFolderDir(fileCategory);
        String logStart = "--start ";
        String logEnd = "--end ";
        // 解析日志文件
        String filePath = fileDir + SOLIDUS + fileName;
        File file = new File(filePath);
        String errLog = fileCategory + " log file " + fileName;
        if (file.isFile()) {                
            try {
                boolean fileIsExist = logFileFacade.checkFileNameExist(fileName);
                if (!fileIsExist) {
                    Date startDate = logFileFacade.getCurrentDBTime();
                    String log = fileCategory + " " + fileName + " log to dbcp----" + new Date();
                    LOG.info(logStart + log);
                    HashMap<String, Object> resultMap = parseToDB(file);
                    LOG.info(logEnd + log);

                    // 保存日志文件记录表
                    saveLogFile(fileName, file.length(), startDate, errLog, resultMap);
                } else {
                    LOG.error(errLog + " have parsed. ");
                }
            } catch (UncategorizedSQLException e) {
                LOG.error(errLog + e);
                nmsUtil.outPutNmsAlarm(errLog + e, NmsUtil.DBEXCPTION, NEConstant.ALARM_LEVEL_CRITICAL);
                fileBackDir = redoFileBackupPath;
            } catch (SQLException e) {
                LOG.error(errLog + e);
                nmsUtil.outPutNmsAlarm(errLog + e, NmsUtil.DBEXCPTION, NEConstant.ALARM_LEVEL_CRITICAL);
                fileBackDir = redoFileBackupPath;
            } catch (IOException e) {
                LOG.error(errLog + e);
                nmsUtil.outPutNmsAlarm(errLog + e, NmsUtil.LOG_FILE_FORMAT_EXCPTION
                    , NEConstant.ALARM_LEVEL_CRITICAL);
                fileBackDir = exceptionFileBackDir;
            } catch (Exception e) {
                LOG.error(errLog + e.getMessage(), e);
                nmsUtil.outPutNmsAlarm(errLog + e, NmsUtil.LOG_FILE_NOT_EXIST_EXCPTION
                    , NEConstant.ALARM_LEVEL_CRITICAL);
                fileBackDir = exceptionFileBackDir;
            } finally {
                LOG.debug(logStart + errLog + " Zip backup and delete file!" + (new Date()));

                boolean compressFlag =
                    compressLogFile
                    .compress(fileName, fileDir, fileBackDir, logFileConfig.getBackUpFileExtension());
                if (!compressFlag) {
                    LOG.error(errLog + " back up error!");
                } else {
                    file.delete();
                }

                LOG.debug(logEnd + errLog + " delete ok" + (new Date()));
                processingFileList.remove(fileName);
                semp.release();
            }
        } else {
            processingFileList.remove(fileName);
            semp.release();
        }
    }

    /**
     * log文件解析完成后保存logFile表中的数据
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param errorLog 错误日志
     * @param paramMap 记录总数、出错记录数
     */
    private void saveLogFile(String fileName, long fileSize, Date startDate, 
        String errorLog, HashMap<String, Object> paramMap) {
        try {
            LogFileVO logFileVO = new LogFileVO();
            logFileVO.setFileName(fileName);
            logFileVO.setFileSize(fileSize);
            logFileVO.setRecordNumber((Integer) paramMap.get(LINENUM));
            logFileVO.setLogFileType(netypeVO.getNeTypeFileType());
            logFileVO.setErrorNumber((Integer) paramMap.get(ERRORNUM));
            logFileVO.setProcessStartDate(startDate);
            logFileFacade.insertLogFile(logFileVO);
        } catch (NumberFormatException e) {
            LOG.error(errorLog + e);
        } catch (SQLException e) {
            LOG.error(errorLog + e);
        } catch (UncategorizedSQLException e) {
            LOG.error(errorLog + e);
        }
    }

    /**
     * 解析，导入数据库
     * @param file 日志文件
     * @return 记录行数、错误记录数（不包含插入DB错误数）
     * @throws IOException 文件读取异常
     * @throws SQLException db操作异常
     */
    public HashMap<String, Object> parseToDB(File file) throws IOException, SQLException {
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(new FileInputStream(file), logFileConfig
                .getFileEncoding(fileCategory)));
        String line;
        String fileName = file.getName();
        int errorNum = 0;
        int lineNum = 0;
        int emptyNum = 0;
        int recordNum = 0;
        String logLineNumber = " lineNum: ";
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        ArrayList<LogXmlPropertyItemVO> propertyList = null;
        HashMap<String, Object> propertyMap = null;
        HashMap<String, Object> propertyMapList = new HashMap<String, Object>();
        HashMap<String, String> keyMap = new HashMap<String, String>();
        Map<String, String> recordMap = new HashMap<String, String>();
        try {
            int batchSize = systemConfigDao.getLogparserBatchSize();
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (!line.isEmpty()) {
                    line = line.trim();
                    String[] columns = line.split("\\|", -1);
                    // 日志记录格式验证
                    if (!stringDataFilter.isfiltered(columns, lineNum)) {
                        propertyMap = new HashMap<String, Object>();
                        // 获取该日志key值
                        if (!setLogKey(columns, keyMap, fileName, lineNum, line)) {
                            errorNum++;
                            continue;
                        }
                        // 获取当前记录对应的logVO
                        LogXmlLogVO logVO = getLog(keyMap);
                        if (null == logVO) {
                            LOG.error(fileCategory + " View Log line:" + lineNum 
                                + " is not a validity log data. File is " + fileName);
                            errorNum++;
                            continue;
                        }
                        propertyList = new ArrayList<LogXmlPropertyItemVO>();
                        propertyList.addAll(netypeVO.getGlobalList());
                        propertyList.addAll(logVO.getPropertyList());
                        // DB项目Map初期化
                        int initPropertyResult = 
                            initPropertyMap(columns, logVO, fileName, lineNum, propertyList, propertyMap);
                        if (initPropertyResult != INIT_SUCCESS) {
                            if (initPropertyResult == INIT_FAIL) {
                                errorNum++;
                            }
                            continue;
                        }
                        if (!doCheck(logVO, fileName, String.valueOf(lineNum), propertyMap, recordMap)) {
                            errorNum++;
                            continue;
                        }
                        // 执行DB操作
                        doDBAction(logVO, file.getName(), lineNum, propertyMap, propertyMapList);
                        recordNum++;
                        if (recordNum >= batchSize) {
                            LOG.debug("the  batch size: " + batchSize + ", start insert");
                            hitvLogParseSqlDao.insertLogData(propertyMapList, file.getName());
                            recordNum = 0;
                            propertyMapList.clear();
                            LOG.debug("the batch size: " + batchSize + ", end insert");
                        }
                        LOG.debug("|||SUC|||" + fileName + logLineNumber + lineNum + COLON
                            + CalculateDate.todayMillisecond());
                        propertyMap = null;
                        propertyList = null;
                    } else {
                        errorNum++;
                        LOG.error(fileName + logLineNumber + lineNum + " data error.");
                    }
                } else {
                    emptyNum++;
                }
            }
            if (recordNum > 0) {
                hitvLogParseSqlDao.insertLogData(propertyMapList, file.getName());
                recordNum = 0;
                propertyMapList.clear();
            }
        } catch (UncategorizedSQLException e) {
            LOG.error(e + " file is " + fileName);
        } catch (IOException e) {
            LOG.debug("reading log file exception:" + fileName + logLineNumber + lineNum);
            throw e;
        } catch (SQLException e) {
            LOG.debug("getLogparserBatchSize exception!!!");
            throw e;
        } finally {
            propertyList = null;
            propertyMap = null;
            reader.close();
        }
        resultMap.put(LINENUM, lineNum - emptyNum);
        resultMap.put(ERRORNUM, errorNum);
        return resultMap;
    }
    
    /**
     * 获取key值，实例化keyMap
     * @param data 当前日志记录
     * @param keyMap 
     * @param fileName 
     * @param lineNum 
     * @param content 
     */
    private boolean setLogKey(String[] data, HashMap<String, String> keyMap, 
        String fileName, int lineNum, String content) {
        try {
            for (LogXmlPropertyItemVO propertyVO : netypeVO.getKeyList()) {
                Integer index = propertyVO.getPropertyIndex();
                keyMap.put(propertyVO.getPropertyId(), data[index]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            LOG.error(e + ": file " + fileName + " lineNum " + lineNum + " content " + content);
            return false;
        }
        return true;
    }

    /**
     * 根据key从netypeVO中取得该记录的具体LogVO
     * @param keyMap 
     * @return 当前记录对应logVO对象
     */
    private LogXmlLogVO getLog(HashMap<String, String> keyMap) {
        LogXmlLogVO logVO = null;
        ArrayList<LogXmlLogVO> logList = netypeVO.getLogList();
        boolean isKeyFind = true;
        String logXmlKeyValue = null;
        for (LogXmlLogVO listItem : logList) {
            for (Entry<String, String> keyEntry : keyMap.entrySet()) {
                logXmlKeyValue = listItem.getKeyValueMap().get(keyEntry.getKey());
                if (null == logXmlKeyValue
                    || !logXmlKeyValue.contains(LogXmlName.COMMA + keyEntry.getValue() + LogXmlName.COMMA)) {
                    isKeyFind = false;
                    break;
                }
            }
            if (isKeyFind) {
                logVO = listItem;
                break;
            } else {
                isKeyFind = true;
            }
        }
        return logVO;
    }

    /**
     * 由配置文件的property映射关系，将该记录的日志项目数值转化为DB项目数值。
     * @param data 当前日志记录
     * @param logVO log配置对象
     * @param fileName 当前日志文件名称
     * @param lineNum 记录行号
     * @param propertyList 属性列表
     * @return true:映射成功，false：映射失败
     */
    private int initPropertyMap(String[] data, LogXmlLogVO logVO, String fileName, Integer lineNum
            , ArrayList<LogXmlPropertyItemVO> propertyList, HashMap<String, Object> propertyMap) {
        int initResult = INIT_SUCCESS;
//        LOG.debug("====Array initPropertyMap:" + data.toString());
        for (LogXmlPropertyItemVO propetyVO : propertyList) {
            initResult = setPropertyValue(data, propetyVO, logVO.getLogId(), fileName, lineNum, propertyMap);
            if (initResult != INIT_SUCCESS) {
                break;
            }
        }
        LOG.debug("====Map initPropertyMap:" + propertyMap);
        return initResult;
    }

    /**
     * 根据property属性将日志记录转化为DB项目数值
     * @param data 日志记录数值
     * @param propetyVO DB项目配置对象
     * @param logType log类型
     * @param fileName 文件名称
     * @param lineNum 记录行号
     * @return true:property设置成功，false：设置失败，0：无效数据
     */
    private int setPropertyValue(String[] data, LogXmlPropertyItemVO propetyVO, String logType, String fileName,
        Integer lineNum, HashMap<String, Object> propertyMap) {
        int setPropertyResult = INIT_SUCCESS;
        Object propertyValue = null;
        String propertyId = propetyVO.getPropertyId();
        // 格式化失败错误信息
        String[] msgParam = new String[ARRAY_COUNT_4];
        msgParam[0] = logType;
        msgParam[1] = String.valueOf(lineNum);
        msgParam[2] = propertyId;
        msgParam[ARRAY_INDEX_3] = fileName;
        try {
            // 取得对应的日志列数据
            if (propetyVO.getPropertyIndex() != -1) {
                propertyValue = data[propetyVO.getPropertyIndex()];
            }
            // 设置默认数值
            else if (propetyVO.getPropertyDefualt() != null) {
                propertyValue = setDefualtValue(propetyVO.getPropertyDefualt(), fileName);
            }
            // 空值校验
            if (!propetyVO.getPropertyNullEnable()
                && propetyVO.getPropertyIndex() != -1
                && (propertyValue == null || "".equals(propertyValue))) {
                outPutErrMsg(msgParam, Message.NULL_VALUE_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
                return INIT_FAIL;
            }
            // 合法性校验
            if (null != propetyVO.getValidationVOList()) {
                Validator validator = new Validator();
                for (LogXmlValidationVO validationVO : propetyVO.getValidationVOList()) {
                    if (!validator.validate(validationVO.getId(), 
                        new Object[] {propertyValue, validationVO.getParams()})) {
                        return INIT_INVALID;
                    }
                }
            }
            propertyMap.put(propertyId, propertyValue);
            // convert处理
            if (null != propetyVO.getConvertVOList()) {
                for (LogXmlConvertVO convertVO : propetyVO.getConvertVOList()) {
                    propertyValue = convertPropertyValue(propertyId, convertVO, propertyMap);
                    if ("defualt2null".equals(convertVO.getConvertType()) || propertyValue != null) {
                        propertyMap.put(propertyId, propertyValue);
                    }
                }
            }

            // 最大长度验证
            Integer maxLen = Integer.parseInt(propetyVO.getPropertyMaxLen().toString());
            if (propertyValue != null && maxLen > 0 && propertyValue.toString().length() > maxLen) {
                // the length is longer than the db column.
                outPutErrMsg(msgParam, Message.MAX_LEN_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
                setPropertyResult = INIT_FAIL;
            }
        } catch (NumberFormatException e) {
            outPutErrMsg(msgParam, Message.FORMAT_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (ParseException e) {
            outPutErrMsg(msgParam, Message.FORMAT_ERR, AlarmDef.CONTENTHASNOTPROGRAMEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (CacheException e) {
            // cache数据取得失败错误信息
            msgParam[2] = e.getMessage();
            outPutErrMsg(msgParam, Message.COMM_ERR, AlarmDef.CONTENTHASNOTPROGRAMEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (ArrayIndexOutOfBoundsException e) {
            // 日志项目下标越界
            msgParam[2] = "Array Index Out Of Bounds, index: " + e.getLocalizedMessage();
            outPutErrMsg(msgParam, Message.COMM_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (MalformedPatternException e) {
            // URL参数取值失败
            msgParam[2] = "Can not parse the value by param  " + propertyId + " in the url ";
            outPutErrMsg(msgParam, Message.COMM_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (UnsupportedEncodingException e) {
            // URL参数取值失败
            msgParam[2] = Message.CANNOTPARSEBYPARAM + propertyId + " in the url";
            outPutErrMsg(msgParam, Message.COMM_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (SecurityException e) {
            msgParam[2] = Message.CANNOTPARSEBYPARAM + propertyId + Message.EXCLAMATION;
            outPutErrMsg(msgParam, Message.FORMAT_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (NoSuchMethodException e) {
            msgParam[2] = Message.CANNOTPARSEBYPARAM + propertyId + Message.EXCLAMATION;
            outPutErrMsg(msgParam, Message.FORMAT_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (IllegalArgumentException e) {
            msgParam[2] = Message.CANNOTPARSEBYPARAM + propertyId + Message.EXCLAMATION;
            outPutErrMsg(msgParam, Message.FORMAT_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (IllegalAccessException e) {
            msgParam[2] = Message.CANNOTPARSEBYPARAM + propertyId + Message.EXCLAMATION;
            outPutErrMsg(msgParam, Message.FORMAT_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
            setPropertyResult = INIT_FAIL;
        } catch (InvocationTargetException e) {
            msgParam[2] = Message.CANNOTPARSEBYPARAM + propertyId + Message.EXCLAMATION;
            outPutErrMsg(msgParam, Message.FORMAT_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
            setPropertyResult = INIT_FAIL;
        }

        return setPropertyResult;
    }

    /**
     * 输出错误信息
     * @param msgParam 错误信息参数
     * @param errMsg 错误信息
     * @param alarmDef 报警类型
     */
    private void outPutErrMsg(String[] msgParam, String errMsg, String alarmDef) {
        String msg = CommonUtil.getMessage(errMsg, msgParam);
        LOG.error(msg);
//        nmsUtil.outPutNmsAlarm(msg, nmsUtil.LOG_FILE_FORMAT_EXCPTION
//            , NEConstant.ALARM_LEVEL_WARNING);
    }

    /**
     * 根据defualt属性设定对应Property数值，一般defualt直接记录对应的DB项目默认值，
     * 但logfileName为当前解析的日志文件名称、today为当前时间。
     * @param defualt defualt属性值
     * @param fileName 当前日志文件名称
     * @return Property的默认值
     */
    private Object setDefualtValue(Object defualt, String fileName) {
        Object defualtValue = null;
        if (LogXmlName.DEFUALT_FILE_NAME.equals(defualt)) {
            defualtValue = fileName;
        } else if (LogXmlName.DEFUALT_TODAY.equals(defualt)) {
            defualtValue = CalculateDate.today();
        } else {
            defualtValue = defualt;
        }
        return defualtValue;
    }

    /**
     * 根据convert规则，转化property的数值
     * @param propertyValue 转化前的property的数值
     * @param convertVO convert规则
     * @throws ParseException
     * @throws NumberFormatException
     * @throws CacheException
     * @throws MalformedPatternException 
     * @throws UnsupportedEncodingException 
     * @throws NoSuchMethodException 
     * @throws IllegalAccessException 
     * @throws InvocationTargetException 
     */
    private Object convertPropertyValue(String propertyName, LogXmlConvertVO convertVO,
        HashMap<String, Object> propertyMap) throws ParseException, 
        NumberFormatException, CacheException, MalformedPatternException, 
        UnsupportedEncodingException, NoSuchMethodException, 
        IllegalAccessException, InvocationTargetException {
        String convertType = convertVO.getConvertType();
        Object convertAfter = null;
        try {
            if (LogXmlName.TAG_CACHE.equals(convertType)) {
                if (null != convertVO.getCaseRef() && null != convertVO.getCaseValue()) {
                    LOG.debug("cache: casevalue start=====");
                    if (convertVO.getCaseValue().equals(propertyMap.get(convertVO.getCaseRef()))) {
                        LOG.debug("case value:" + convertVO.getCaseValue() + "caseref:" + convertVO.getCaseRef()
                            + "property value" + propertyMap.get(convertVO.getCaseRef()));
                        // 循环处理各ref参数
                        convertAfter = getCacheValue(convertVO, propertyMap);
                    }
                    LOG.debug("cache: casevalue end=====");
                } else {
                    // 循环处理各ref参数
                    convertAfter = getCacheValue(convertVO, propertyMap);
                }
            } else {
                Formattor formattor = new Formattor(convertType);
                formattor.setConvertVO(convertVO);
                formattor.setPropertyMap(propertyMap);
                formattor.setPropertyName(propertyName);
                convertAfter = formattor.format();
            } 
        } catch (NumberFormatException e) {
            LOG.error(e.getMessage());
            throw e;
        } catch (CacheException e) {
            LOG.error(e.getMessage());
            throw e;
        } catch (ParseException e) {
            LOG.error(e.getMessage());
            throw e;
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage());
            throw e;
        } catch (MalformedPatternException e) {
            LOG.error(e.getMessage());
            throw e;
        } catch (SecurityException e) {
            LOG.error(e.getMessage());
            throw e;
        } catch (NoSuchMethodException e) {
            LOG.error(e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            throw e;
        } catch (IllegalAccessException e) {
            LOG.error(e.getMessage());
            throw e;
        } catch (InvocationTargetException e) {
            LOG.error(e.getMessage());
            throw e;
        }
        return convertAfter;
    }

    /**
     * 获取cache对象value
     * @param convertVO cache转化节点对象
     * @return cache对象value
     * @throws CacheException
     */
    private Object getCacheValue(LogXmlConvertVO convertVO, HashMap<String, Object> propertyMap) throws CacheException {
        Object convertAfter;
        String strRef = convertVO.getRefProperty();
        if (!strRef.contains(COMMA) && propertyMap.get(strRef) == null) {
            return null;
        }
        String [] arrRef = strRef.split(COMMA);
        String strRefvalue = "";
        for (String strRefItem : arrRef) {
            strRefvalue = strRefvalue + COMMA + propertyMap.get(strRefItem);
        }
        if (strRefvalue.startsWith(COMMA)) {
            strRefvalue = strRefvalue.substring(1, strRefvalue.length());
        }
        convertAfter =
            getCacheValueByKey(convertVO.getCachePrefix(), strRefvalue);
        return convertAfter;
    }
    
    /**
     * 对propertyMap中数据作相关验证，异常场合输出错误日志。
     * @param logVO log配置对象
     * @param fileName log配置对象
     * @param lineNum log配置对象
     * @param recordMap 日志记录
     * @return boolean true：Check通过 false：Check失败
     */
    private boolean doCheck(LogXmlLogVO logVO, String fileName, String lineNum, 
        HashMap<String, Object> propertyMap, Map<String, String> recordMap) {
        boolean isNotError = true;
        ArrayList<LogXmlCheckItemVO> checkList = logVO.getCheckList();
        String checkType = null;
        if (null == checkList) {
            return isNotError;
        }
        for (LogXmlCheckItemVO checkVO : checkList) {
            checkType = checkVO.getCheckType();
            if (LogXmlName.CHECK_IS_UNIQUE.equals(checkType)) {
                isNotError =
                    doUniqueCheck(checkVO.getCheckPropertyMap(), logVO.getLogId(), fileName, 
                        lineNum, propertyMap, recordMap);
            } else if (LogXmlName.CHECK_DATE_COMPARE.equals(checkType)) {
                isNotError =
                    doDateCompareCheck(checkVO.getCheckPropertyMap(), logVO.getLogId(), fileName, lineNum, propertyMap);
            } else if (LogXmlName.CHECK_DATE_EMPTY.equals(checkType)) {
                isNotError =
                    doDataNotAllEmptyCheck(checkVO.getCheckPropertyMap(), logVO.getLogId(), fileName, 
                        lineNum, propertyMap);
            }
            if (!isNotError) {
                break;
            }
        }
        return isNotError;
    }
    
    /**
     * 校验属性是否全为空
     * @param checkPropertyMap check项目列表
     * @param logType log类型
     * @param fileName 文件名称
     * @param lineNum 记录行号
     * @param recordMap 日志记录
     * @return boolean true:check成功 fale:check失败
     */
    private boolean doDataNotAllEmptyCheck(HashMap<String, String> checkPropertyMap, String logType, 
        String fileName, String lineNum, HashMap<String, Object> propertyMap) {
        boolean result = false;
        StringBuffer checkMsg = new StringBuffer();
        for (Entry<String, String> checkIte : checkPropertyMap.entrySet()) {
            Object obj = propertyMap.get(checkIte.getKey());
            if (obj != null && !"".equals(obj.toString().trim())) {
                result = true;
                break;
            }
            checkMsg.append(",");
            checkMsg.append(checkIte.getKey());
        }
        if (!result) {
            checkMsg.deleteCharAt(0);
            String[] msgParam = new String[ARRAY_COUNT_4];
            msgParam[0] = logType;
            msgParam[1] = lineNum;
            msgParam[2] = checkMsg.toString();
            msgParam[ARRAY_INDEX_3] = fileName;
            outPutErrMsg(msgParam, Message.ALLEMPTY_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
        }
        return result;
    }

    /**
     * 当前文件内唯一性Check
     * @param checkPropertyMap check项目列表
     * @param logType log类型
     * @param fileName 文件名称
     * @param lineNum 记录行号
     * @param recordMap 日志记录
     * @return boolean true:check成功 fale:check失败
     */
    private boolean doUniqueCheck(HashMap<String, String> checkPropertyMap, String logType, String fileName,
        String lineNum, HashMap<String, Object> propertyMap, Map<String, String> recordMap) {
        boolean isExist = true;
        StringBuffer checkMsg = new StringBuffer();
        checkMsg.append(logType);
        checkMsg.append(SOLIDUS);
        for (Entry<String, String> checkIte : checkPropertyMap.entrySet()) {
            checkMsg.append(checkIte.getKey());
            checkMsg.append(COLON);
            checkMsg.append(propertyMap.get(checkIte.getKey()));
        }
        isExist = CommonUtil.isUnique(recordMap, checkMsg.toString());
        if (!isExist) {
            String[] msgParam = new String[ARRAY_COUNT_4];
            msgParam[0] = logType;
            msgParam[1] = lineNum;
            msgParam[2] = checkMsg.toString();
            msgParam[ARRAY_INDEX_3] = fileName;
            outPutErrMsg(msgParam, Message.UNIQUE_ERR, AlarmDef.COLUMNISNOTCORRECTEXCEPTION);
        }
        return isExist;
    }

    private boolean doDateCompareCheck(HashMap<String, String> checkPropertyMap, String logType, String fileName,
        String lineNum, HashMap<String, Object> propertyMap) {
        boolean isNotError = true;
        Date startTime = (Date) propertyMap.get("startTime");
        Date endTime = (Date) propertyMap.get("endTime");
        if (startTime.getTime() > endTime.getTime()) {
            isNotError = false;
            String[] msgParam = new String[ARRAY_COUNT_3];
            msgParam[0] = logType;
            msgParam[1] = lineNum;
            msgParam[2] = fileName;
            outPutErrMsg(msgParam, Message.TIME_COM_ERR, AlarmDef.STARTTIMEFORMATEXCEPTION);
        }
        return isNotError;
    }

    /**
     * 根据配置文件相关设置将propertyMap中数据写入DB。
     * @param logVO log配置对象
     * @throws SQLException
     */
    private void doDBAction(LogXmlLogVO logVO, String fileName, int lineNum, 
        HashMap<String, Object> propertyMap, HashMap<String, Object> propertyMapList) {
        HashMap<String, ArrayList<LogXmlActionItemVO>> actionMap = logVO.getActionMap();

        ArrayList<LogXmlActionItemVO> sqlList = actionMap.get("oracle");
//        try {
        for (LogXmlActionItemVO actionItemVO : sqlList) {

            boolean isDoAction = isDoAction(actionItemVO, propertyMap);
            if (isDoAction) {
                if (LogXmlName.DB_INSERT.equals(actionItemVO.getActionType())) {
                    propertyMapList.put(lineNum + SOLIDUS + actionItemVO.getSqlId(), propertyMap);
                } else if (LogXmlName.DB_UPDATE.equals(actionItemVO.getActionType())) {
                    // TODO 暂没有该处理
                }
            }
        }
//        } catch (Exception e) {
//            StringBuffer errMsg = new StringBuffer();
//            errMsg.append(logVO.getLogId());
//            errMsg.append("View Log line:");
//            errMsg.append(lineNum);
//            errMsg.append(" File is ");
//            errMsg.append(fileName);
//            errMsg.append(COLON);
//            errMsg.append(e.getMessage());
//            LOG.error(errMsg.toString());
//            AlarmBroadcast.getInstance().sendAlarm(
//                new AlarmInfo(Long.parseLong(AlarmDef.getAlarm(fileCategory, AlarmDef.INSERTEXCEPTION)), CalculateDate
//                    .today().getTime(), AlarmLevel.MAJOR, errMsg.toString()));
//        } 
    }

    /**
     * 根据action参数判断本行数据是否进行DB操作
     * @param actionItemVO action节点对象
     * @param propertyMap db项目键值对
     */
    private boolean isDoAction(LogXmlActionItemVO actionItemVO, HashMap<String, Object> propertyMap) {
        String caseRef = actionItemVO.getCaseRef();
        String caseValue = actionItemVO.getCaseValue();
        boolean isDoAction = false;
        if (null != caseRef && null != caseValue) {
            String[] refArray = caseRef.split(COMMA);
            String propertyValue = "";
            int arrLen = refArray.length;
            for (int i = 0; i < arrLen; i++) {
                propertyValue = propertyValue + propertyMap.get(refArray[i]);
                if (i < arrLen - 1) {
                    propertyValue = propertyValue + COMMA;
                }
            }
            if (propertyValue.equals(caseValue)) {
                isDoAction = true;
            }
        } else if (null == caseRef && null == caseValue) {
            isDoAction = true;
        }
        return isDoAction;
    }
 
}
