/**
 * 
 */
package com.hisense.hitv.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.hisense.hitv.nms.javaagent.NE;
import com.hisense.hitv.nms.javaagent.dto.Param;

/**
 * @author zhoudi
 */
public class AlarmNE implements NE {
    private static Logger logger = Logger.getLogger(AlarmNE.class);
    /**
     * 
     */
    @Override
	public void getConfig() {
        logger.info("begin get config");
    }

    /**
     * 
     * @param arg0 
     */
    @Override
	public void setConfig(List<Param> arg0) {
        logger.info("begin set config");
    }
}
