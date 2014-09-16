package com.hisense.hitv.programcontent.persistence.sqldao;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.hisense.hitv.programcontent.persistence.IServiceChannelDao;
import com.hisense.hitv.programcontent.domain.ServiceChannelVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: felicia Date: 2008-8-21 Time: 9:42:30
 */
public class ServiceChannelImpl extends SqlMapClientDaoSupport implements IServiceChannelDao {

    /**
     * 获取所有serviceid、channelid数据列表
     * @return ServiceChannelVO列表
     * @throws DataAccessException DB操作异常
     */
    @Override
	@SuppressWarnings("unchecked")
    public List<ServiceChannelVO> getAllServiceChannel() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAllServiceChannel", null);
    }

    /**
     * 根据serviceid查找channelid
     * @param serviceId 检索用serviceid
     * @return ServiceChannelVO对象
     * @throws DataAccessException DB操作异常
     */
    @Override
	public ServiceChannelVO getServiceChannelById(long serviceId) throws DataAccessException {
        return (ServiceChannelVO) getSqlMapClientTemplate().queryForObject("getServiceChannelById", serviceId);
    }
}
