package com.hisense.hitv.cache.persistence.sqldao;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.hisense.hitv.cache.domain.ChannelTimeProgramVO;
import com.hisense.hitv.cache.persistence.IChannelTimeProgramDao;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: felicia Date: 2008-8-21 Time: 9:42:30
 */
public class ChannelTimeProgramDaoImpl extends SqlMapClientDaoSupport implements IChannelTimeProgramDao {

    /**
     * 获取所有channelid,starttime、programename数据列表
     * @param paramMap 参数Map
     * @return ChannelTimeProgramVO列表
     * @throws DataAccessException DB操作异常
     */
    @SuppressWarnings("unchecked")
    public List<ChannelTimeProgramVO> getAllChannelTimeProgram(Map<String, Object> paramMap) 
        throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAllChannelTimeProgram", paramMap);
    }

    /**
     * 根据channelid\starttime查找programname\programcode
     * @param channelTimeVO 检索用channelId及观看时间
     * @return ChannelTimeProgramVO对象
     * @throws DataAccessException DB操作异常
     */
    public ChannelTimeProgramVO getProgramByChannelTime(ChannelTimeProgramVO channelTimeVO) throws DataAccessException {
        return (ChannelTimeProgramVO) getSqlMapClientTemplate().queryForObject("getProgramByChannelTime",
            channelTimeVO);
    }
}
