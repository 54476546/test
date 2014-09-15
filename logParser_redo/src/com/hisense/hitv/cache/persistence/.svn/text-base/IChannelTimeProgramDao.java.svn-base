package com.hisense.hitv.cache.persistence;

import com.hisense.hitv.cache.domain.ChannelTimeProgramVO;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * ChannelTimeProgram检索接口
 * @author zhoudi
 * @version 1.0
 */
public interface IChannelTimeProgramDao {
    /**
     * 获取所有channelid,starttime、programename\programcode数据列表
     * @param paramMap 参数Map
     * @return ChannelTimeProgramVO列表
     * @throws DataAccessException DB操作异常 
     */
    public List<ChannelTimeProgramVO> getAllChannelTimeProgram(Map<String, Object> paramMap) throws DataAccessException;
    /**
     * 根据channelid\starttime查找programname\programcode
     * @param channelTimeVO 检索用channelId及观看时间
     * @return ChannelTimeProgramVO对象
     * @throws DataAccessException DB操作异常 
     */
    public ChannelTimeProgramVO getProgramByChannelTime(ChannelTimeProgramVO channelTimeVO) throws 
    DataAccessException;
}
