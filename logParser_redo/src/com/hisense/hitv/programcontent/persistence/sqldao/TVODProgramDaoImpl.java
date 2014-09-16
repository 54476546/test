package com.hisense.hitv.programcontent.persistence.sqldao;

import com.hisense.hitv.programcontent.domain.TVODProgramVO;
import com.hisense.hitv.programcontent.persistence.ITVODProgramDao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * TVODProgram检索接口实现类
 * @author zhoudi
 * @version 1.0
 */
public class TVODProgramDaoImpl extends SqlMapClientDaoSupport implements ITVODProgramDao {
    /**
     * 检索出所有TVODProgram记录
     * @return TVODProgram记录列表
     * @throws DataAccessException DB操作异常
     */
    @Override
	@SuppressWarnings("unchecked")
    public List<TVODProgramVO> getAllTVODProgram() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAllTVODProgram", null);
    }

    /**
     * 根据ProgramId检索TVODProgram记录
     * @param programId ProgramId
     * @return TVODProgram记录对象
     * @throws DataAccessException DB操作异常
     */
    @Override
	public TVODProgramVO getTVODProgramById(long programId) throws DataAccessException {
        return (TVODProgramVO) getSqlMapClientTemplate().queryForObject("getTVODProgramById", programId);
    }
}
