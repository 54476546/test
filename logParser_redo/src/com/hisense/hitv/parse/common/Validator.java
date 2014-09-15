package com.hisense.hitv.parse.common;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.oro.text.regex.MalformedPatternException;
/**
 * 校验器
 * @author tianyuqi
 */
public class Validator {
    
    /**
     * 构造方法
     */
    public Validator() {
    }
    
    /**
     * 
     * @param methodName 方法名
     * @param args 参数
     * @return Object
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws NumberFormatException 
     * @throws InvocationTargetException 
     * @throws UnsupportedEncodingException 
     * @throws MalformedPatternException 
     */
    @SuppressWarnings("unchecked")
    public Boolean validate(String methodName, Object[] args) throws SecurityException, 
        IllegalArgumentException, IllegalAccessException, NumberFormatException,
        InvocationTargetException, UnsupportedEncodingException, 
        MalformedPatternException, NoSuchMethodException {
        Class[] argsClass = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argsClass[i] = args[i].getClass();
        }
        Method method = getClass().getMethod(methodName, argsClass);
        return (Boolean) method.invoke(this, args);
    }
    
    /**
     * 根据参数内容判断是否有效
     * @param keyValue 被判断的值
     * @param invalidValue 无效的值列表
     * @return true/false
     */
    public Boolean isValid(String keyValue, String invalidValue) {
        String[] invalidValueList = invalidValue.split(",");
        for (int i = 0; i < invalidValueList.length; i++) {
            if (invalidValueList[i].trim().equals(keyValue)) {
                return false;
            }
        }
        return true;
    }
}
