package com.hisense.hitv.subscriber.persistence;

import com.hisense.hitv.subscriber.domain.InternetSubscriberVO;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * InternetSubscriber检索接口
 * @author zhoudi
 * @version 1.0
 */
public interface IInternetSubscriberDao {
    /**
     * 检索出所有InternetSubscriber记录
     * @return Subscriber记录列表
     * @throws DataAccessException DB操作异常
     */
    public List<InternetSubscriberVO> getInternetSubscribers() throws DataAccessException;

    /**
     * 根据SubscriberId检索InternetSubscriber记录
     * @param subscriberId SubscriberId
     * @return InternetSubscriber记录对象
     * @throws DataAccessException DB操作异常
     */
    public InternetSubscriberVO getInternetSubscriberById(long subscriberId) throws DataAccessException;

}
