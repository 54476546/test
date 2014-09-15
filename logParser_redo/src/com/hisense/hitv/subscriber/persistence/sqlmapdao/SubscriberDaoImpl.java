package com.hisense.hitv.subscriber.persistence.sqlmapdao;

import com.hisense.hitv.subscriber.domain.SubscriberVO;
import com.hisense.hitv.subscriber.persistence.ISubscriberDao;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

/**
 * Subscriber检索接口实现类
 * @author zhoudi
 * @version 1.0
 */
public class SubscriberDaoImpl extends SqlMapClientDaoSupport implements ISubscriberDao {

    /**
     * 根据SubscriberId检索Subscriber记录
     * @param subscriberId SubscriberId
     * @return Subscriber记录对象
     * @throws DataAccessException DB操作异常
     */
    public SubscriberVO getSubscriberById(long subscriberId) throws DataAccessException {
        return (SubscriberVO) getSqlMapClientTemplate().queryForObject("getSubscriberById", subscriberId);
    }

    /**
     * 根据Mac检索Subscriber记录
     * @param mac Mac
     * @return Subscriber记录对象
     * @throws DataAccessException DB操作异常
     */
    public SubscriberVO getSubscriberByMac(String mac) throws DataAccessException {
        return (SubscriberVO) getSqlMapClientTemplate().queryForObject("getSubscriberByMac", mac);
    }

    /**
     * 检索用户总数
     * @return 用户总数
     * @throws DataAccessException DB操作异常
     */
    public Integer getSubscriberCount() throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("getSubscriberCount");
    }

    /**
     * 分页检索所有Subscriber记录
     * @param subscriberVO Subscriber数据对象
     * @return Subscriber记录列表
     * @throws DataAccessException DB操作异常
     */
    @SuppressWarnings("unchecked")
    public List<SubscriberVO> getSubscribers(SubscriberVO subscriberVO) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getSubscribers", subscriberVO);
    }

}
