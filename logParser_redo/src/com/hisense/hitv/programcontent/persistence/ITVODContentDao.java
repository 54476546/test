package com.hisense.hitv.programcontent.persistence;

import com.hisense.hitv.programcontent.domain.TVODContentVO;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * TVODContent检索接口
 * @author zhoudi
 * @version 1.0
 */
public interface ITVODContentDao {

    /**
     * 根据ContentId检索TVODContent记录
     * @param contentId ContentId
     * @return TVODContent记录对象
     * @throws DataAccessException DB操作异常
     */
    public TVODContentVO getTVODByContentId(long contentId) throws DataAccessException;

    /**
     * 检索出所有TVODContent记录
     * @return TVODContent记录列表
     * @throws DataAccessException DB操作异常
     */
    public List<TVODContentVO> getAllTVODContent() throws DataAccessException;
}
