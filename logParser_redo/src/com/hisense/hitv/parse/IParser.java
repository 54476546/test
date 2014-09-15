package com.hisense.hitv.parse;

import com.hisense.hitv.resource.domain.LogXmlNetypeVO;

/**
 * 日志解析Parser接口，继承于Runnable，实现多线程
 * @author zhoudi
 * @version 1.0
 */
public interface IParser extends Runnable {
    /**
     * 启动解析处理
     */
    public void processLogFile();
    /**
     * 传递netype网元对象
     * @param netypeVO 网元VO对象
     */
    public void setNetype(LogXmlNetypeVO netypeVO);
}
