package com.hisense.hitv.programcontent.domain;

/**
 * TVODContent记录对象
 * @author zhoudi
 * @version 1.0
 */
public class ProgramContentVO {

    private long contentId;
    private long programId;
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
    /**
     * @param contentId the contentId to set
     */
    public void setContentId(long contentId) {
        this.contentId = contentId;
    }
    /**
     * @return the contentId
     */
    public long getContentId() {
        return contentId;
    }
}