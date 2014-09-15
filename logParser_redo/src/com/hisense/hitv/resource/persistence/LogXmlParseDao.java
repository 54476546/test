package com.hisense.hitv.resource.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hisense.hitv.resource.constant.LogXmlName;
import com.hisense.hitv.resource.domain.LogXmlActionItemVO;
import com.hisense.hitv.resource.domain.LogXmlCheckItemVO;
import com.hisense.hitv.resource.domain.LogXmlConvertVO;
import com.hisense.hitv.resource.domain.LogXmlLogVO;
import com.hisense.hitv.resource.domain.LogXmlNetypeVO;
import com.hisense.hitv.resource.domain.LogXmlPropertyItemVO;
import com.hisense.hitv.resource.domain.LogXmlValidationVO;
import com.hisense.hitv.service.LogParserService;

/**
 * 日志配置文件加载
 * @author zhoudi
 * @version 1.0
 */
public class LogXmlParseDao implements LogXmlName {
    private static final Log LOG = LogFactory.getLog(LogXmlParseDao.class);
    
    private String filePostFix = ".xml";
    private String stvfileDir = "/usr/logparser/config/logModel";
    private String ngb40fileDir = "/usr/logparser/config/logModelNGB";
    private ArrayList<LogXmlNetypeVO> netypeList =
        new ArrayList<LogXmlNetypeVO>();

    /**
     * 构造函数
     */
    public LogXmlParseDao() {
    }

    /**
     * @return the netypeList
     */
    public ArrayList<LogXmlNetypeVO> getNetypeList() {
        return netypeList;
    }

    /**
     * 加载日志配置文件
     * @return true:成功，false：失败
     */
    @SuppressWarnings("unchecked")
    public boolean load() {
        LOG.debug("LogXmlParseDao method load() start");
        boolean isLoadSuc = true;
        netypeList.clear();
        // 日志配置文件读取
        File file = null;
        if (LogParserService.LGTYPE_STV == LogParserService.getCurLGType()) {
            file = new File(stvfileDir);
        } else if (LogParserService.LGTYPE_NGB40 == LogParserService.getCurLGType()) {
            file = new File(ngb40fileDir);
        }
        if (null == file) {
            LOG.debug("Log Module xml filepath is error.");
            return false;
        }
        File[] fileList = file.listFiles();
        String fileName = null;
        Integer xmlFileCount = 0;
        for (File logModel : fileList) {
            fileName = logModel.getName();
            // 日志配置文件格式检查
            if (fileName != null) {
                if (fileName.indexOf(filePostFix) < 1) {
                    continue;
                }
            }
            xmlFileCount = xmlFileCount + 1;
            // 日志配置文件读取
            SAXReader saxReader = new SAXReader();
            saxReader.setValidation(true);
            Document document = null;
            try {
                document = saxReader.read(logModel);
                Element rootElment = document.getRootElement();

                // netype节点加载
                Iterator<Element> elementIte =
                    rootElment.elementIterator(TAG_NETYPE);
                while (elementIte.hasNext()) {
                    Element netypeElement = elementIte.next();
                    LogXmlNetypeVO netypeVO = parseNetypeNode(netypeElement);
                    if (netypeVO != null) {
                        netypeList.add(netypeVO);
                    }
                }
            } catch (DocumentException e) {
                LOG.error(e.getMessage());
                isLoadSuc = false;
                break;
//            } catch (NumberFormatException e) {
//                LOG.error(e.getMessage());
//                isLoadSuc = false;
//                AlarmBroadcast.getInstance().sendAlarm(
//                    new AlarmInfo(Long.parseLong(AlarmDef.getAlarm(LOGXML, AlarmDef.EXCEPTION)),
//                        CalculateDate.today().getTime(), AlarmLevel.MINOR, e.getMessage()));
//                break;
            } finally {
                saxReader = null;
            }
        }
        if (xmlFileCount == 0 &&  isLoadSuc) {
            isLoadSuc = false;
            LOG.debug("Not have Log Module xml file.");
        } else if (isLoadSuc) {
            LOG.debug("Log Module xml parse success!");
        }
        LOG.debug("LogXmlParseDao method load() end");
        return isLoadSuc;
    }

    /**
     * netype节点解析
     * @param netypeElement netype节点
     * @return LogXmlNetypeVO netype节点VO对象
     */
    @SuppressWarnings("unchecked")
    private LogXmlNetypeVO parseNetypeNode(Element element) {
        LOG.debug("LogXmlParseDao method parseNetypeNode() start");
        LogXmlNetypeVO netypeVO = null;
        if (element != null) {
            netypeVO = new LogXmlNetypeVO();
            netypeVO.setNeTypeId(element.attributeValue(ATTRIBUTE_ID));
            String fileType = element.attributeValue(ATTRIBUTE_LOG_FILE_TYPE);
            try {
                if (null != fileType) {
                    netypeVO.setNeTypeFileType(Integer.valueOf(fileType));
                }
            } catch (NumberFormatException e) {
                throw e;
            }
            // key节点解析
            netypeVO.setKeyList(getPropertyList(element.element(TAG_KEY)));
            // Global节点解析
            netypeVO
                .setGlobalList(getPropertyList(element.element(TAG_GLOBAL)));
            // log节点解析
            Iterator<Element> logIte = element.elementIterator(TAG_LOG);
            ArrayList<LogXmlLogVO> logList = new ArrayList<LogXmlLogVO>();
            while (logIte.hasNext()) {
                Element logElement = logIte.next();
                LogXmlLogVO logVO = parseLogNode(logElement);
                logList.add(logVO);
            }
            netypeVO.setLogList(logList);
        }
        LOG.debug("LogXmlParseDao method parseNetypeNode() end");
        return netypeVO;
    }

    /**
     * 解析log节点
     * @param element log节点
     * @return LogXmlLogVO log节点VO对象
     */
    @SuppressWarnings("unchecked")
    private LogXmlLogVO parseLogNode(Element element) {
        LOG.debug("LogXmlParseDao method parseLogNode() start");
        LogXmlLogVO logVO = new LogXmlLogVO();
        logVO.setLogId(element.attributeValue(ATTRIBUTE_ID));        
        // logVO.setLogVersion(element.attributeValue(ATTRIBUTE_VERSION));
        // logVO.setLogType(element.attributeValue(ATTRIBUTE_TYPE));
        // primarykey节点解析
        Iterator<Element> primaryKeyIte =
            element.elementIterator(TAG_PRIMARY_KEY);
        HashMap<String, String> keyValueMap =
            getPrimaryKeyValue(primaryKeyIte.next());
        logVO.setKeyValueMap(keyValueMap);
        // properties节点解析
        Iterator<Element> propertyIte = element.elementIterator(TAG_PROPERTIES);
        ArrayList<LogXmlPropertyItemVO> propertyList =
            getPropertyList(propertyIte.next());
        logVO.setPropertyList(propertyList);
        // checks节点解析
        Iterator<Element> checkIte = element.elementIterator(TAG_CHECKS);
        if (checkIte.hasNext()) {
            ArrayList<LogXmlCheckItemVO> checkList =
                getCheckList(checkIte.next());
            logVO.setCheckList(checkList);
        }
        // actionmap节点解析
        Iterator<Element> actionMapIte = element.elementIterator(TAG_ACTIONMAP);
        HashMap<String, ArrayList<LogXmlActionItemVO>> actionMap =
            parseActionMapNode(actionMapIte.next());
        logVO.setActionMap(actionMap);
        LOG.debug("LogXmlParseDao method parseLogNode() end");
        return logVO;
    }

    /**
     * 解析keyproperty节点获取key项目的value Map
     * @param element
     * @return
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, String> getPrimaryKeyValue(Element element) {
        LOG.debug("LogXmlParseDao method getPrimaryKeyValue() start");
        HashMap<String, String> keyValueMap = new HashMap<String, String>();
        Iterator<Element> keyPropertyIte =
            element.elementIterator(TAG_KEY_PROPERTY);
        while (keyPropertyIte.hasNext()) {
            String keyValue = COMMA;
            Element keyPropertyElement = keyPropertyIte.next();
            List<Element> keyValueElementList = keyPropertyElement.elements();
            for (Element keyValueElement : keyValueElementList) {
                keyValue = keyValue 
                    + keyValueElement.attributeValue(ATTRIBUTE_DATA) + COMMA;
            }
            keyValueMap.put(keyPropertyElement.attributeValue(ATTRIBUTE_ID),
                keyValue);
        }
        LOG.debug("LogXmlParseDao method getPrimaryKeyValue() end");
        return keyValueMap;
    }

    /**
     * 解析key、global、properties节点获取property列表
     * @param element key、global、properties节点
     * @return property列表
     */
    @SuppressWarnings("unchecked")
    private ArrayList<LogXmlPropertyItemVO> getPropertyList(Element element) {
        LOG.debug("LogXmlParseDao method getPropertyList() start");
        ArrayList<LogXmlPropertyItemVO> propertyList = null;
        Iterator<Element> propertyIte = element.elementIterator(TAG_PROPERTY);
        if (!propertyIte.hasNext()) {
            return propertyList;
        } else {
            propertyList = new ArrayList<LogXmlPropertyItemVO>();
            while (propertyIte.hasNext()) {
                Element propertyElement = propertyIte.next();
                LogXmlPropertyItemVO propertyVO =
                    parsePropertyItemNode(propertyElement);
                if (propertyVO != null) {
                    propertyList.add(propertyVO);
                }
            }
        }
        LOG.debug("LogXmlParseDao method getPropertyList() end");
        return propertyList;
    }

    /**
     * 解析property节点
     * @param element property节点
     * @return LogXmlPropertyItemVO property节点VO对象
     */
    @SuppressWarnings("unchecked")
    private LogXmlPropertyItemVO parsePropertyItemNode(Element element) {
        LOG.debug("LogXmlParseDao method parsePropertyItemNode() start");
        LogXmlPropertyItemVO propertyVO = new LogXmlPropertyItemVO();
        propertyVO.setPropertyId(element.attributeValue(ATTRIBUTE_ID));
        String index = element.attributeValue(ATTRIBUTE_INDEX);
        String maxLen = element.attributeValue(ATTRIBUTE_MAX_LEN);
        String nullEnable = element.attributeValue(ATTRIBUTE_NULL_ENABLE);
        try {
            if (null != index) {
                propertyVO.setPropertyIndex(Integer.valueOf(index));
            }
            if (null != maxLen) {
                propertyVO.setPropertyMaxLen(Integer.valueOf(maxLen));
            }
            propertyVO.setPropertyNullEnable(Boolean.parseBoolean(nullEnable));
        } catch (NumberFormatException e) {
            throw e;
        }
        propertyVO
            .setPropertyDefualt(element.attributeValue(ATTRIBUTE_DEFUALT));

        // convert节点解析
        Iterator<Element> convertIte = element.elementIterator(TAG_CONVERT);
        while (convertIte.hasNext()) {
            Element convertElement = convertIte.next();
            ArrayList<LogXmlConvertVO> convertVOList =
                parseConvertNode(convertElement);
            if (convertVOList != null) {
                propertyVO.setConvertVOList(convertVOList);
            }
        }
        
        // validations节点解析
        Iterator<Element> validationIte = element.elementIterator(TAG_VALIDATION);
        while (validationIte.hasNext()) {
            Element validationElement = validationIte.next();
            ArrayList<LogXmlValidationVO> validationVOList =
                parseValidationNode(validationElement);
            if (validationVOList != null) {
                propertyVO.setValidationVOList(validationVOList);
            }
        }
        LOG.debug("LogXmlParseDao method parsePropertyItemNode() end");
        return propertyVO;
    }

    /**
     * 解析convert节点获取convert节点VO对象
     * @param element convert节点
     * @return convert节点VO对象
     */
    @SuppressWarnings("unchecked")
    private ArrayList<LogXmlConvertVO> parseConvertNode(Element element) {
        LOG.debug("LogXmlParseDao method parseConvertNode() start");
        ArrayList<LogXmlConvertVO> convertVOList = null;
        LogXmlConvertVO convertVO = null;
        // 遍历该convert节点的所有子节点
        Iterator<Element> convertItemIte = element.elementIterator();
        String convertType = null;
        String convertName = null;
        String refProperty = null;
        String cachePrefix = null;
        String casevalue = null;
        String caseref = null;
        String refIndex = null;
        if (convertItemIte.hasNext()) {
            convertVOList = new ArrayList<LogXmlConvertVO>();
            while (convertItemIte.hasNext()) {
                convertVO = new LogXmlConvertVO();
                Element convertItemElement = convertItemIte.next();
                convertName = convertItemElement.getName();
                if (TAG_FORMAT.equals(convertName)) {
                    convertType =
                        convertItemElement.attributeValue(ATTRIBUTE_TYPE);
                    refProperty =
                        convertItemElement.attributeValue(ATTRIBUTE_REF);
                    refIndex =
                        convertItemElement.attributeValue(ATTRIBUTE_REF_INDEX);
                } else if (TAG_CACHE.equals(convertName)) {
                    convertType = convertName;
                    refProperty =
                        convertItemElement.attributeValue(ATTRIBUTE_REF);
                    cachePrefix =
                        convertItemElement.attributeValue(ATTRIBUTE_PREFIX);
                    casevalue =
                        convertItemElement.attributeValue(ATTRIBUTE_CASE_VALUE);
                    caseref =
                        convertItemElement.attributeValue(ATTRIBUTE_CASE_REF);
                }
                convertVO.setConvertType(convertType);
                convertVO.setRefProperty(refProperty);
                convertVO.setCachePrefix(cachePrefix);
                convertVO.setCaseValue(casevalue);
                convertVO.setCaseRef(caseref);
                convertVO.setRefIndex(refIndex);
                convertVOList.add(convertVO);
            }
        }
        LOG.debug("LogXmlParseDao method parseConvertNode() end");
        return convertVOList;
    }
    
    /**
     * 解析validations节点获取validation节点VO对象
     * @param element validations节点
     * @return validation节点VO对象
     */
    @SuppressWarnings("unchecked")
    private ArrayList<LogXmlValidationVO> parseValidationNode(Element element) {
        LOG.debug("LogXmlParseDao method parseValidationNode() start");
        ArrayList<LogXmlValidationVO> validationVOList = null;
        LogXmlValidationVO validationVO = null;
        // 遍历该validations节点的所有子节点
        Iterator<Element> validationItemIte = element.elementIterator();
        if (validationItemIte.hasNext()) {
            validationVOList = new ArrayList<LogXmlValidationVO>();
            while (validationItemIte.hasNext()) {
                validationVO = new LogXmlValidationVO();
                Element validationItemElement = validationItemIte.next();
                validationVO.setId(validationItemElement.attributeValue(ATTRIBUTE_ID));
                validationVO.setParams(validationItemElement.attributeValue(ATTRIBUTE_PARAMS));
                validationVOList.add(validationVO);
            }
        }
        LOG.debug("LogXmlParseDao method parseValidationNode() end");
        return validationVOList;
    }

    /**
     * 解析checks节点获取check列表
     * @param element check节点
     * @return check列表
     */
    @SuppressWarnings("unchecked")
    private ArrayList<LogXmlCheckItemVO> getCheckList(Element element) {
        LOG.debug("LogXmlParseDao method getCheckList() start");
        ArrayList<LogXmlCheckItemVO> checkList = null;
        Iterator<Element> checkIte = element.elementIterator(TAG_CHECK);
        if (!checkIte.hasNext()) {
            return checkList;
        } else {
            checkList = new ArrayList<LogXmlCheckItemVO>();
            while (checkIte.hasNext()) {
                Element checkElement = checkIte.next();
                LogXmlCheckItemVO checkVO = parseCheckItemNode(checkElement);
                if (checkVO != null) {
                    checkList.add(checkVO);
                }
            }
        }
        LOG.debug("LogXmlParseDao method getCheckList() end");
        return checkList;
    }

    /**
     * 解析check节点获取check节点VO对象
     * @param element check节点
     * @return check节点VO对象
     */
    @SuppressWarnings("unchecked")
    private LogXmlCheckItemVO parseCheckItemNode(Element element) {
        LOG.debug("LogXmlParseDao method parseCheckItemNode() start");
        LogXmlCheckItemVO checkVO = new LogXmlCheckItemVO();
        checkVO.setCheckType(element.attributeValue(ATTRIBUTE_ID));
        // check子节点解析
        List<Element> childrenIte = element.elements();
        HashMap<String, String> checkPropertyMap =
            new HashMap<String, String>();
        for (Element childrenElement : childrenIte) {
            checkPropertyMap.put(childrenElement.attributeValue(ATTRIBUTE_ID),
                childrenElement.getName());
        }
        checkVO.setCheckPropertyMap(checkPropertyMap);
        LOG.debug("LogXmlParseDao method parseCheckItemNode() end");
        return checkVO;
    }

    /**
     * 解析actionmap节点获取action列表
     * @param element actionmap节点
     * @return actionMap[key：DB类型、value:DB操作列表]
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, ArrayList<LogXmlActionItemVO>> parseActionMapNode(
        Element element) {
        LOG.debug("LogXmlParseDao method parseActionMapNode() start");
        HashMap<String, ArrayList<LogXmlActionItemVO>> actionMap =
            new HashMap<String, ArrayList<LogXmlActionItemVO>>();
        Iterator<Element> actionIte = element.elementIterator(TAG_ACTION);
        while (actionIte.hasNext()) {
            Element actionElement = actionIte.next();
            actionMap.put(actionElement.attributeValue(ATTRIBUTE_DBTYPE),
                parseActionNode(actionElement));
        }
        LOG.debug("LogXmlParseDao method parseActionMapNode() end");
        return actionMap;
    }

    /**
     * 解析Action节点，获取DB操作列表
     * @param element action节点
     * @return action节点VO对象列表
     */
    @SuppressWarnings("unchecked")
    private ArrayList<LogXmlActionItemVO> parseActionNode(Element element) {
        LOG.debug("LogXmlParseDao method parseActionNode() start");
        ArrayList<LogXmlActionItemVO> actionItemList =
            new ArrayList<LogXmlActionItemVO>();
        Iterator<Element> actionItemIte = element.elementIterator();
        while (actionItemIte.hasNext()) {
            Element actionItemElement = actionItemIte.next();
            LogXmlActionItemVO actionItemVO = new LogXmlActionItemVO();
            actionItemVO.setActionType(actionItemElement.getName());
            actionItemVO.setSqlId(actionItemElement
                .attributeValue(ATTRIBUTE_SQLID));
            actionItemVO.setNameSpace(actionItemElement
                .attributeValue(ATTRIBUTE_NAMESPACE));
            actionItemVO.setCaseRef(actionItemElement
                .attributeValue(ATTRIBUTE_CASE_REF));
            actionItemVO.setCaseValue(actionItemElement
                .attributeValue(ATTRIBUTE_CASE_VALUE));
            actionItemList.add(actionItemVO);
        }
        LOG.debug("LogXmlParseDao method parseActionNode() end");
        return actionItemList;
    }

}
