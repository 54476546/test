package com.hisense.hitv.dbcp;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 继承Spring PropertyPlaceholderConfigurer类，
 * 扩展其mergeProperties私有方法，对外提供Properties结果
 * @author zhoudi
 * @version 1.0
 */
public class HitvPropertyConfigurer extends PropertyPlaceholderConfigurer {

    private Properties pros;
    
    /**
     * construct function
     */
    public HitvPropertyConfigurer() {
        super();
    }
    /**
     * @return the pros
     * @throws IOException 
     */
    public Properties getPros() throws IOException {
        this.pros = super.mergeProperties();
        return pros;
    }    
}
