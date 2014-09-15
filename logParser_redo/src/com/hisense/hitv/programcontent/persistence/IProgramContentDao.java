package com.hisense.hitv.programcontent.persistence;

import com.hisense.hitv.programcontent.domain.ProgramContentVO;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * ProgramContent检索接口
 * @author zhoudi
 * @version 1.0
 */
public interface IProgramContentDao {
    /**
     * 检索出所有ProgramContent记录
     * @return ProgramContent记录列表
     * @throws DataAccessException DB操作异常
     */
    public List<ProgramContentVO> getAllVODContent() throws DataAccessException;
    /**
     * 根据ContentId检索ProgramContent记录
     * @param contentId ContentId
     * @return ProgramContent记录对象
     * @throws DataAccessException DB操作异常
     */
    public ProgramContentVO getVODContentById(long contentId) throws DataAccessException;
}
