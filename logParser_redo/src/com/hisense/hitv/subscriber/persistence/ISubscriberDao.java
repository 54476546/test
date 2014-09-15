package com.hisense.hitv.subscriber.persistence;

import com.hisense.hitv.subscriber.domain.SubscriberVO;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * Subscriber检索接口
 * @author zhoudi
 * @version 1.0
 */
public interface ISubscriberDao {

    /**
     * 根据SubscriberId检索Subscriber记录
     * @param subscriberId SubscriberId
     * @return Subscriber记录对象
     * @throws DataAccessException DB操作异常
     */
    public SubscriberVO getSubscriberById(long subscriberId) throws DataAccessException;

    /**
     * 根据Mac检索Subscriber记录
     * @param mac Mac
     * @return Subscriber记录对象
     * @throws DataAccessException DB操作异常
     */
    public SubscriberVO getSubscriberByMac(String mac) throws DataAccessException;

    /**
     * 检索用户总数
     * @return 用户总数
     * @throws DataAccessException DB操作异常
     */
    public Integer getSubscriberCount() throws DataAccessException;

    /**
     * 分页检索出所有Subscriber记录
     * @param subscriberVO Subscriber数据对象
     * @return Subscriber记录列表
     * @throws DataAccessException DB操作异常
     */
    public List<SubscriberVO> getSubscribers(SubscriberVO subscriberVO) throws DataAccessException;
}
