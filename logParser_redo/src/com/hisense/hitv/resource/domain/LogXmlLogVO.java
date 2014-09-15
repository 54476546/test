package com.hisense.hitv.resource.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * log节点对象
 * @author zhoudi
 * @version 1.0
 */
public class LogXmlLogVO {

    private String logId = null;
    private HashMap<String, String> keyValueMap = null;
    private ArrayList<LogXmlPropertyItemVO> propertyList = null;
    private ArrayList<LogXmlCheckItemVO> checkList = null;
    private HashMap<String, ArrayList<LogXmlActionItemVO>> actionMap = null;
    /**
     * @return the logId
     */
    public String getLogId() {
        return logId;
    }
    /**
     * @param logId the logId to set
     */
    public void setLogId(String logId) {
        this.logId = logId;
    }
    
    /**
     * @return the keyValueMap
     */
    public HashMap<String, String> getKeyValueMap() {
        return keyValueMap;
    }
    /**
     * @param keyValueMap the keyValueMap to set
     */
    public void setKeyValueMap(HashMap<String, String> keyValueMap) {
        this.keyValueMap = keyValueMap;
    }
    /**
     * @return the propertyList
     */
    public ArrayList<LogXmlPropertyItemVO> getPropertyList() {
        return propertyList;
    }
    /**
     * @param propertyList the propertyList to set
     */
    public void setPropertyList(ArrayList<LogXmlPropertyItemVO> propertyList) {
        this.propertyList = propertyList;
    }
    /**
     * @return the checkList
     */
    public ArrayList<LogXmlCheckItemVO> getCheckList() {
        return checkList;
    }
    /**
     * @param checkList the checkList to set
     */
    public void setCheckList(ArrayList<LogXmlCheckItemVO> checkList) {
        this.checkList = checkList;
    }
    /**
     * @return the actionMap
     */
    public HashMap<String, ArrayList<LogXmlActionItemVO>> getActionMap() {
        return actionMap;
    }
    /**
     * @param actionMap the actionMap to set
     */
    public void setActionMap(
        HashMap<String, ArrayList<LogXmlActionItemVO>> actionMap) {
        this.actionMap = actionMap;
    }
}
