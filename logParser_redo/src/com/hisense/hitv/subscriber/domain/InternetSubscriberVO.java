package com.hisense.hitv.subscriber.domain;

/**
 * InternetSubscriber记录对象
 * @author zhoudi
 * @version 1.0
 */
public class InternetSubscriberVO {
    private long subscriberId;
    private String deviceCode;
    /**
     * @return the subscriberId
     */
    public long getSubscriberId() {
        return subscriberId;
    }
    /**
     * @param subscriberId the subscriberId to set
     */
    public void setSubscriberId(long subscriberId) {
        this.subscriberId = subscriberId;
    }
    /**
     * @return the deviceCode
     */
    public String getDeviceCode() {
        return deviceCode;
    }
    /**
     * @param deviceCode the deviceCode to set
     */
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

}
