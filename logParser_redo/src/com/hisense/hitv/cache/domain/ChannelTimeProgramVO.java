package com.hisense.hitv.cache.domain;

/**
 * ChannelTimeProgram记录对象
 * @author zhoudi
 * @version 1.0
 */
public class ChannelTimeProgramVO {

    private long channelId;
    private long startTime;
    private long endTime;
    private String programCode;
    private String programName;
    private long eventId;
    
    /**
     * @param channelId the channelId to set
     */
    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }
    /**
     * @return the channelId
     */
    public long getChannelId() {
        return channelId;
    }
    
    /**
     * @param programCode the programCode to set
     */
    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }
    /**
     * @return the programCode
     */
    public String getProgramCode() {
        return programCode;
    }
    /**
     * @param programName the programName to set
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }
    /**
     * @return the programName
     */
    public String getProgramName() {
        return programName;
    }
    /**
     * @param eventId the eventId to set
     */
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
    /**
     * @return the eventId
     */
    public long getEventId() {
        return eventId;
    }
    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    /**
     * @return the endTime
     */
    public long getEndTime() {
        return endTime;
    }
    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }
   
}