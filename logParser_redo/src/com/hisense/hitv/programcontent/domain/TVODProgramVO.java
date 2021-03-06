package com.hisense.hitv.programcontent.domain;

/**
 * TVODProgram记录对象
 * @author zhoudi
 * @version 1.0
 */
public class TVODProgramVO {
    private long programId;
    private long channelId;

    /**
     * construct function
     */
    public TVODProgramVO() {
        this.setProgramId(-1);
        this.setChannelId(-1);
    }

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
     * @param programId the programId to set
     */
    public void setProgramId(long programId) {
        this.programId = programId;
    }

    /**
     * @return the programId
     */
    public long getProgramId() {
        return programId;
    }
}
