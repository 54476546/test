package com.hisense.hitv.parse.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import com.hisense.hitv.resource.domain.LogXmlConvertVO;
import com.hisense.hitv.util.CalculateDate;
import com.hisense.hitv.util.CommonUtil;

/**
 * 
 * @author tianyuqi
 *
 */
public class Formattor {
    private static final Log LOG = LogFactory.getLog(Formattor.class);
    private static final String FILEPATH = "/usr/logparser/config/format.cfg";
    private static final String COMMA = ",";
    private static final String PLUS = "+";
    private static final String ENCODING = "UTF-8";
    private static Properties properties = new Properties();
    private String type;
    private String propertyName;
    private LogXmlConvertVO convertVO;
    private HashMap<String, Object> propertyMap;

    /**
     * 
     */
    public Formattor() {
        
    }
    
    /**
     * 
     * @param type 
     */
    public Formattor(String type) {
        this.type = type;
    }
    
    static {
        try {
            InputStream stream = null;
            if (System.getProperty("os.name").toLowerCase()
                    .indexOf("windows") != -1) {
                stream = new BufferedInputStream(new FileInputStream(FILEPATH));
            } else {
                stream = new BufferedInputStream(new FileInputStream(FILEPATH));
            }

            properties.load(stream);            
        } catch (IOException e) {
            LOG.error(e.toString());
        }
    }
    
    /**
     * 
     * @return convertVO
     */
    public LogXmlConvertVO getConvertVO() {
        return convertVO;
    }

    /**
     * 
     * @param convertVO 
     */
    public void setConvertVO(LogXmlConvertVO convertVO) {
        this.convertVO = convertVO;
    }
    
    /**
     * 
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 
     * @param propertyName 
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 
     * @return propertyMap
     */
    public HashMap<String, Object> getPropertyMap() {
        return propertyMap;
    }

    /**
     * 
     * @param propertyMap 
     */
    public void setPropertyMap(HashMap<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }

    /**
     * 
     * @return Object
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws NumberFormatException 
     * @throws InvocationTargetException 
     * @throws UnsupportedEncodingException 
     * @throws MalformedPatternException 
     * @throws ParseException 
     */
    public Object format() throws SecurityException, NoSuchMethodException,
        IllegalArgumentException, IllegalAccessException, NumberFormatException,
        InvocationTargetException, UnsupportedEncodingException, 
        MalformedPatternException, ParseException {
        if (properties.containsKey(type)) {
            Method method = getClass().getMethod(
                properties.getProperty(type), null);
            return method.invoke(this, null);
        }
        return null;
        
    }
    
    /**
     * 
     * @param methodName 
     * @param args 
     * @return Object
     */
    public Object format(String methodName, Object[] args) {

        Class[] argsClass = new Class[args.length];

        for (int i = 0; i < args.length; i++) {
            argsClass[i] = args[i].getClass();
        }
        
        try {
            Method method = getClass().getMethod(methodName, argsClass);
            return method.invoke(this, args);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * 
     * @return Object
     * @throws NumberFormatException 
     * @throws ParseException 
     */
    public Object formatDate() throws NumberFormatException, ParseException {
        Object convertBefore = propertyMap.get(propertyName);
        if (convertBefore.toString().length() > 10) {
            throw new ParseException("Time is overlength!", 0);
        }
        return (Object) CalculateDate.parseSecLong2Date(
            Long.parseLong(convertBefore.toString()));
    }
    

    /**
     * 
     * @return Object
     * @throws NumberFormatException 
     * @throws ParseException 
     */
    public Object formatMSDate() throws NumberFormatException, ParseException {
        Object convertBefore = propertyMap.get(propertyName);
        if (convertBefore.toString().length() > 13) {
            throw new ParseException("MSTime is overlength!", 0);
        }
        return (Object) CalculateDate.parseMsLongToDate(
            Long.parseLong(convertBefore.toString()));
    }
    
    /**
     * 
     * @return Object
     * @throws ParseException 
     */
    public Object format15StrDate() throws ParseException {
        Object convertBefore = propertyMap.get(propertyName);
        return (Object) CalculateDate.parse15StrToDate(
            convertBefore.toString());
    }
    
    /**
     * 
     * @return Object
     * @throws ParseException 
     */
    public Object formatMonth() throws ParseException {
        Object convertBefore = propertyMap.get(convertVO.getRefProperty());
        return (Object) CalculateDate.parseLongDateMonth(
            ((Date) convertBefore).getTime());
    }
    
    /**
     * 
     * @return Object
     * @throws ParseException 
     */
    public Object formatWeek() throws ParseException {
        Object convertBefore = propertyMap.get(convertVO.getRefProperty());
        return CalculateDate.parseLongDateWeek(
            ((Date) convertBefore).getTime());
    }
    
    /**
     * 
     * @return Object
     * @throws ParseException 
     */
    public Object formatDay() throws ParseException {
        Object convertBefore = propertyMap.get(convertVO.getRefProperty());
        return CalculateDate.getDateDay((Date) convertBefore);
    }
    
    /**
     * 
     * @return Object
     * @throws NumberFormatException 
     */
    public Object formatSignum() throws NumberFormatException {
        Object convertBefore = propertyMap.get(propertyName);
        return Integer.parseInt(convertBefore.toString().replace(PLUS, ""));
    }
    
    /**
     * 
     * @return Object
     * @throws NumberFormatException 
     */
    public Object formatLength() throws NumberFormatException {
        Object convertBefore = propertyMap.get(propertyName);
        int length = Integer.parseInt(convertVO.getRefProperty());
        return CommonUtil.getLeftStr(convertBefore.toString(), length);
    }
    
    /**
     * 
     * @return Object
     */
    public Object formatDefault2Null() {
        Object convertBefore = propertyMap.get(propertyName);
        if (convertVO.getRefProperty().equals(convertBefore)) {
            return null;
        } else {
            return convertBefore;
        }
    }
    
    /**
     * 
     * @return Object
     * @throws NumberFormatException 
     * @throws UnsupportedEncodingException 
     */
    public Object formatArr() 
        throws NumberFormatException, UnsupportedEncodingException {
        Object convertBefore = propertyMap.get(convertVO.getRefProperty());
        return getValueFromArray(convertVO, convertBefore);
    }
    
    /**
     * 
     * @return Object
     * @throws NumberFormatException 
     */
    public Object formatLong() throws NumberFormatException {
        Object convertBefore = propertyMap.get(propertyName);
        return Long.parseLong(convertBefore.toString());
    }
    
    /**
     * 
     * @return Object
     * @throws UnsupportedEncodingException 
     * @throws MalformedPatternException 
     */
    public Object formatURL() 
        throws UnsupportedEncodingException, MalformedPatternException {
        Object convertBefore = propertyMap.get(convertVO.getRefProperty());
        return getValueFromUrl(propertyName, convertBefore.toString());
    }
    
    /**
     * 根据小标从【，】分割的字符串中获取转化后的数值
     * @param convertVO 转化节点对象
     * @param convertBefore 【，】分割的字符串
     * @return 转化后的数值
     * @throws NumberFormatException index下标错误
     * @throws UnsupportedEncodingException 
     */
    private Object getValueFromArray(
        LogXmlConvertVO convertVO, Object convertBefore)
        throws NumberFormatException, UnsupportedEncodingException {
        Object convertAfter = null;
        int refIndex = convertVO.getRefIndex() 
            == null ? -1 : Integer.parseInt(convertVO.getRefIndex());
        if (null != convertBefore && refIndex != -1) {
            convertAfter = URLDecoder.decode(
                convertBefore.toString().split(COMMA)[refIndex], ENCODING);
        }
        return convertAfter;
    }
    
    /**
     * 根据参数名称从URL中获取参数对应的数值
     * @param paramName 参数名
     * @param convertBefore url
     * @return 参数值
     * @throws MalformedPatternException
     * @throws UnsupportedEncodingException 
     */
    private Object getValueFromUrl(Object paramName, String convertBefore) 
        throws MalformedPatternException, UnsupportedEncodingException {
        Object paramValue = null;
//        convertBefore = "Action=EPG_SSS&ListKey=1&SearchKey=中@国、|<SearchKey=>&SearcheType=1";
        String regExp = "&*(" + paramName + ")=([^&]*)&*";
        PatternCompiler compiler = new Perl5Compiler(); 
        PatternMatcher matcher = new Perl5Matcher();
        Pattern pattern = compiler.compile(regExp);
        if (matcher.contains(convertBefore, pattern)) {
            MatchResult result = matcher.getMatch(); 
            paramValue = URLDecoder.decode(result.group(2), ENCODING);
        }
        return paramValue;
    }
    
}
