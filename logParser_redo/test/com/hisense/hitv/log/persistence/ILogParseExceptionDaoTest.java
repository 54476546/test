package com.hisense.hitv.log.persistence;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hisense.hitv.util.SpringUtil;
/**
 * 测试类
 * @author tianyuqi
 */
public class ILogParseExceptionDaoTest {

    @Test
    public void insertLogSqlException_A$Map() throws Exception {
        ILogParseExceptionDao target = (ILogParseExceptionDao) SpringUtil.getBean("logParseExceptionDao");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("logfilename", "123.log");
        paramMap.put("linenumber", "12");
        paramMap.put("exceptionmessage", "EXCEPTIONMESSAGE1");
        try {
            target.insertLogSqlException(paramMap);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
