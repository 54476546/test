package com.hisense.hitv.dbcp.domain;

/**
 * DBUser属性对象，用于接收动态查询获取的用户信息。
 * @author zhoudi
 * @version 1.0
 */
public class HitvDBUserVO {

    private String dbUser;
    private String dbPassword;
    /**
     * @param dbPassword the dbPassword to set
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    /**
     * @return the dbPassword
     */
    public String getDbPassword() {
        return dbPassword;
    }
    /**
     * @param dbUser the dbUser to set
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }
    /**
     * @return the dbUser
     */
    public String getDbUser() {
        return dbUser;
    }
}
