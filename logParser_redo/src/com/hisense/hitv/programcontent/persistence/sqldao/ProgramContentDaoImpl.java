package com.hisense.hitv.programcontent.persistence.sqldao;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.hisense.hitv.programcontent.persistence.IProgramContentDao;
import com.hisense.hitv.programcontent.domain.ProgramContentVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: felicia Date: 2008-8-21 Time: 9:42:30
 */
public class ProgramContentDaoImpl extends SqlMapClientDaoSupport implements
    IProgramContentDao {

    /**
     * 获取所有VOD contentId、programId数据列表
     * @return ProgramContentVO列表
     * @throws DataAccessException DB操作异常 
     */
    @Override
	@SuppressWarnings("unchecked")
    public List<ProgramContentVO> getAllVODContent() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getAllVODContent", null);
    }

    /**
     * 根据contentId查找programId
     * @param contentId 检索用contentId
     * @return ProgramContentVO对象
     * @throws DataAccessException DB操作异常 
     */
    @Override
	public ProgramContentVO getVODContentById(long contentId) throws 
    DataAccessException {
        return (ProgramContentVO) getSqlMapClientTemplate().queryForObject(
            "getVODContentById", contentId);
    }
}
