package com.hisense.hitv.programcontent.persistence;

import com.hisense.hitv.programcontent.domain.TVODProgramVO;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * TVODProgram检索接口
 * @author zhoudi
 * @version 1.0
 */
public interface ITVODProgramDao {
    /**
     * 检索出所有TVODProgram记录
     * @return TVODProgram记录列表
     * @throws DataAccessException DB操作异常
     */
    public List<TVODProgramVO> getAllTVODProgram() throws DataAccessException;

    /**
     * 根据ProgramId检索TVODProgram记录
     * @param programId ProgramId
     * @return TVODProgram记录对象
     * @throws DataAccessException DB操作异常
     */
    public TVODProgramVO getTVODProgramById(long programId) throws DataAccessException;
}
