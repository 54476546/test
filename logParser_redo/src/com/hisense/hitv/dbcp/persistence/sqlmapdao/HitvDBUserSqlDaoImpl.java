package com.hisense.hitv.dbcp.persistence.sqlmapdao;

import com.hisense.hitv.dbcp.persistence.IHitvDBUserDao;
import com.hisense.hitv.dbcp.domain.HitvDBUserVO;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * 用户信息查询接口实现类
 * @author zhoudi
 * @version 1.0
 */
public class HitvDBUserSqlDaoImpl extends SqlMapClientDaoSupport implements IHitvDBUserDao {

    private String applicationContext;

    /**
     * @param applicationContext the applicationContext to set
     */
    public void setApplicationContext(String applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * @return the applicationContext
     */
    public String getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据pubuser查询动态配置的用户信息
     * @param dbUser pubuser名
     * @return 用户信息
     */
    public HitvDBUserVO getHitvDBUserByUser(String dbUser) {
        return (HitvDBUserVO) getSqlMapClientTemplate().queryForObject("getHitvDBUserByUser", dbUser);
    }
}
