package com.hisense.hitv.subscriber.persistence.sqlmapdao;

import com.hisense.hitv.subscriber.persistence.IInternetSubscriberDao;
import com.hisense.hitv.subscriber.domain.InternetSubscriberVO;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * InternetSubscriber检索接口
 * @author zhoudi
 * @version 1.0
 */
public class InternetSubscriberDaoImpl extends SqlMapClientDaoSupport implements IInternetSubscriberDao {
    /**
     * 检索出所有InternetSubscriber记录
     * @return Subscriber记录列表
     * @throws DataAccessException DB操作异常
     */
    @Override
	@SuppressWarnings("unchecked")
    public List<InternetSubscriberVO> getInternetSubscribers() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getInternetSubscribers", null);
    }

    /**
     * 根据SubscriberId检索InternetSubscriber记录
     * @param subscriberId SubscriberId
     * @return InternetSubscriber记录对象
     * @throws DataAccessException DB操作异常
     */
    @Override
	public InternetSubscriberVO getInternetSubscriberById(long subscriberId) throws DataAccessException {
        return (InternetSubscriberVO) getSqlMapClientTemplate().queryForObject("getInternetSubscriberById",
            subscriberId);
    }
}
