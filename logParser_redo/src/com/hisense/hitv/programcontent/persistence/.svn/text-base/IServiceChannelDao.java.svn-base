package com.hisense.hitv.programcontent.persistence;

import com.hisense.hitv.programcontent.domain.ServiceChannelVO;

import java.util.List;
import java.sql.SQLException;

/**
 * ProgramContent检索接口
 * @author zhoudi
 * @version 1.0
 */
public interface IServiceChannelDao {
    /**
     * 获取所有serviceid、channelid数据列表
     * @return ServiceChannelVO列表
     * @throws SQLException DB操作异常 
     */
    public List<ServiceChannelVO> getAllServiceChannel() throws SQLException;
    /**
     * 根据serviceid查找channelid
     * @param serviceId 检索用serviceid
     * @return ServiceChannelVO对象
     * @throws SQLException DB操作异常 
     */
    public ServiceChannelVO getServiceChannelById(long serviceId) throws 
    SQLException;
}
