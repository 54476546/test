package com.hisense.hitv.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring工具类
 * @author tianyuqi
 * @version 1.0
 */
public class SpringUtil {
    /**
     * 配置文件路径
     */
    public static final String[] SPRINGCONTEXTCONFIGFILE =
    {"config/dataAccessContext-local.xml", "config/applicationContext.xml"};
    private static ApplicationContext context;
    private static SpringUtil instance = new SpringUtil();

    private SpringUtil() {
        try {
            context =
                new ClassPathXmlApplicationContext(SPRINGCONTEXTCONFIGFILE);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    /**
     * 获取实例
     * @return instance
     */
    public static SpringUtil getInstance() {
        return instance;
    }
    /**
     * 获取context
     * @return context
     */
    public static ApplicationContext getContext() {
        return SpringUtil.getInstance().context;
    }
    /**
     * 获取bean
     * @param name 
     * @return bean 
     */
    public static Object getBean(String name) {
        return getContext().getBean(name);
    }
}
