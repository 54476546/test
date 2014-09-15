package com.hisense.hitv.programcontent.persistence.sqldao;

import com.hisense.hitv.programcontent.domain.TVODContentVO;
import com.hisense.hitv.programcontent.persistence.ITVODContentDao;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

/**
 * TVODProgram检索接口实现类
 * @author zhoudi
 * @version 1.0
 */
public class TVODContentDaoImpl extends SqlMapClientDaoSupport implements ITVODContentDao {
    /**
     * 根据TVODContent检索TVODContent记录
     * @param contentId ContentId
     * @return TVODContent记录对象
     * @throws DataAccessException DB操作异常
     */
    public TVODContentVO getTVODByContentId(long contentId) throws DataAccessException {
        return (TVODContentVO) getSqlMapClientTemplate().queryForObject("getTVODByContentId", contentId);
    }

    /**
     * 检索出所有TVODContent记录
     * @return TVODContent记录列表
     * @throws DataAccessException DB操作异常
     */
    @SuppressWarnings("unchecked")
    public List<TVODContentVO> getAllTVODContent() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAllTVODContent", null);
    }
}
