package com.hisense.hitv.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期计算相关方法
 * @author zhoudi
 * @version 1.0
 */
public class CalculateDate {
    /**
     * 
     */
    private static final int MULTIPLE_1000 = 1000;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

//    private static final DateFormat YYYYMM = new SimpleDateFormat("yyyyMM");
//    private static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat YYYYMMDDHHMMSS =
        new SimpleDateFormat("yyyyMMddHHmmss");
//    private static final DateFormat YYYY_MM_DD =
//        new SimpleDateFormat("yyyy-MM-dd");
//    private static final DateFormat YYYY_MM_DD_HH_MM_SS =
//        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat YYYY_MM_DD_HH_MM_SS_SSS =
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    /**
     * constructor
     */
    private CalculateDate() {
        
    }
    
    /**
     * 解析s级的long型日期
     * @param dateLong 为s级
     * @return date类型的日期
     */
    public static Date parseMsLongToDate(long dateLong) {
        return new Date(dateLong);
    }

    /**
     * 解析s级的long型日期
     * @param timeSecond s级时间
     * @return date类型的日期
     */
    public static Date parseSecLong2Date(long timeSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeSecond * MULTIPLE_1000);

        return calendar.getTime();
    }

    /**
     * 解析日期对应的每周中的星期
     * @param date date类型日期
     * @return 星期
     */
    public static int parseLongDateWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 默认星期天为1
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }
    /**
     * 解析日期对应的每周中的星期
     * @param inputDate long类型日期
     * @return 星期
     * @throws ParseException 转化异常
     */
    public static int parseLongDateWeek(long inputDate) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(inputDate);
        // 默认星期天为1
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }
//    /**
//     * 解析日期对应的分
//     * @param inputDate long类型日期
//     * @return 分
//     * @throws ParseException 转化异常
//     */
//    public static int parseLongDateMinute(long inputDate) throws ParseException {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(inputDate);
//
//        return calendar.get(Calendar.MINUTE);
//    }
//    /**
//     * 解析日期对应的小时
//     * @param inputDate long类型日期
//     * @return 小时
//     * @throws ParseException 转化异常
//     */
//    public static int parseLongDateHour(long inputDate) throws ParseException {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(inputDate);
//
//        return calendar.get(Calendar.HOUR_OF_DAY);
//    }
//    /**
//     * 解析日期对应的日
//     * @param inputDate long类型日期
//     * @return 日
//     * @throws ParseException 转化异常
//     */
//    public static int parseLongDateDay(long inputDate) throws ParseException {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(inputDate);
//
//        return calendar.get(Calendar.DAY_OF_MONTH);
//    }
    /**
     * 解析日期对应的月
     * @param inputDate long类型日期
     * @return 月
     * @throws ParseException 转化异常
     */
    public static int parseLongDateMonth(long inputDate) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(inputDate);

        return calendar.get(Calendar.MONTH) + 1; // 1月份默认为0
    }
//
//    /**
//     * 格式化日期为YYYY_MM_DD，在返回对应的星期
//     * @param inputDate string类型日期
//     * @return 星期
//     * @throws ParseException 转化异常
//     */
//    public static int getYYYY_MM_DDInDateWeek(String inputDate)
//        throws ParseException {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(YYYY_MM_DD.parse(inputDate));
//        // 默认星期天为1
//        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
//    }
//    /**
//     * 格式化日期为YYYY_MM_DD，在返回对应的月
//     * @param inputDate string类型日期
//     * @return 月
//     * @throws ParseException 转化异常
//     */
//    public static int getYYYY_MM_DDInDateMonth(String inputDate)
//        throws ParseException {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(YYYY_MM_DD.parse(inputDate));
//
//        return calendar.get(Calendar.MONTH) + 1; // 1月份默认为0
//    }
//    /**
//     * 格式化日期为YYYY_MM_DD的date对象
//     * @param inputDate string类型日期
//     * @return date日期
//     * @throws ParseException 转化异常
//     */
//    public static Date parseYYYY_MM_DDtrToDate(String inputDate)
//        throws ParseException {
//        return YYYY_MM_DD.parse(inputDate);
//    }
//    /**
//     * 格式化日期为YYYY_MM_DD_HH_MM_SS的date对象
//     * @param inputDate string类型日期
//     * @return date日期
//     * @throws ParseException 转化异常
//     */
//    public static Date parseYYYY_MM_DD_HH_MM_SSStrToDate(String inputDate)
//        throws ParseException {
//        return YYYY_MM_DD_HH_MM_SS.parse(inputDate);
//    }
//    /**
//     * 格式化日期为YYYY_MM_DD_HH_MM_SS_SSS的date对象
//     * @param inputDate string类型日期
//     * @return date日期
//     * @throws ParseException 转化异常
//     */
//    public static Date parseYYYY_MM_DD_HH_MM_SS_SSSStrToDate(String inputDate)
//        throws ParseException {
//        return YYYY_MM_DD_HH_MM_SS_SSS.parse(inputDate);
//    }
    /**
     * 格式化日期为YYYYMMDDHHMMSS的date对象
     * @param inputDate string类型日期
     * @return date日期
     * @throws ParseException 转化异常
     */
    public static Date parse15StrToDate(String inputDate)
        throws ParseException {
        return YYYYMMDDHHMMSS.parse(inputDate);
    }
//    /**
//     * 格式化日期为YYYYMMDD的string对象
//     * @param date Date类型日期
//     * @return string日期
//     */
//    public static String formatYYYYMMDD(Date date) {
//        return YYYYMMDD.format(date);
//    }

//    public static String formatYYYYMMDDHHMMSS() {
//        Date date = new Date();
//        return YYYYMMDDHHMMSS.format(date);
//    }
//
//    public static String formatYYYYMMDDHHMMSS(Date date) {
//        return YYYY_MM_DD_HH_MM_SS.format(date);
//    }
//
//    public static String formatYYYYMMDDHHMMSSSSS(Date date) {
//        return YYYY_MM_DD_HH_MM_SS_SSS.format(date);
//    }
//
//    public static String todayYYYYMMDD() {
//        Date date = new Date();
//        return YYYYMMDD.format(date);
//    }

    /**
     * 按照YYYY_MM_DD_HH_MM_SS_SSS格式化当前日期
     * @return Date类型日期
     */
    public static String todayMillisecond() {
        Date date = new Date();

        return YYYY_MM_DD_HH_MM_SS_SSS.format(date);
    }
//
//    public static String todayYYYY_MM_DD() {
//        Date date = new Date();
//
//        return YYYY_MM_DD.format(date);
//    }
//
//    public static int getDateMinute(Date date) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//
//        return time.get(Calendar.MINUTE);
//    }
//
//    public static int getDateWeek(Date date) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//
//        return time.get(Calendar.DAY_OF_WEEK) - 1; // SUNDAY - 0
//    }
//
//    public static int getDateMonth(Date date) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//
//        return time.get(Calendar.MONTH) + 1;
//    }
//
//    public static int getDateHour(Date date) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//
//        return time.get(Calendar.HOUR_OF_DAY);
//    }
    /**
     * 取得每月的第n天
     * @param date 计算日期
     * @return int 每月的第n天
     */
    public static int getDateDay(Date date) {
        GregorianCalendar time = new GregorianCalendar();
        time.setTime(date);

        return time.get(Calendar.DAY_OF_MONTH);
    }
//
//    public static long addDateMinuteAndZeroSec(Date date, int minute) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//        time.add(Calendar.MINUTE, minute);
//        time.set(Calendar.SECOND, 0);
//
//        return time.getTime().getTime();
//    }
//
//    public static Date setDateMinuteAndZeroSec(Date date, int minute) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//        time.set(Calendar.MINUTE, minute);
//        time.set(Calendar.SECOND, 0);
//
//        return time.getTime();
//    }
//
//    public static Date setDateMinuteZeroSec(Date date) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//        time.set(Calendar.SECOND, 0);
//
//        return time.getTime();
//    }
//
//    public static Date addDateHour(Date date, int hour) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//        time.add(Calendar.HOUR_OF_DAY, hour);
//
//        return time.getTime();
//    }
//
//    public static Date addDateMintuer(Date date, int minute) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//        time.add(Calendar.MINUTE, minute);
//
//        return time.getTime();
//    }
//
//    public static Date addDateDay(Date date, int day) {
//        GregorianCalendar time = new GregorianCalendar();
//        time.setTime(date);
//        time.add(Calendar.DAY_OF_MONTH, day);
//
//        return time.getTime();
//    }
//
    /**
     * 获取当前时间
     * @return 当前时间
     */
    public static Date today() {
        return new Date();
    }

//    public static void main(String[] args) throws ParseException {
        /*
         * Date temp1 =
         * CalculateDate.parseYYYY_MM_DD_HH_MM_SSStrToDate("2008-09-04 04:00:00"
         * ); System.out.println("temp1 = " + temp1); Date temp =
         * CalculateDate.parseSecLong2Date(1239758940 / 1000);
         * System.out.println("1236614400 - 24*3600 = " + (1236614400 - 24
         * 3600)); System.out.println("temp = " + temp); String[] zones =
         * TimeZone.getAvailableIDs(-8 60 60 1000);
         * System.out.println("zones.toString() = " + Arrays.asList(zones));
         * long time = (new Date()).getTime(); System.out.println("time = " +
         * time / 1000);
         */

//        Date date = CalculateDate.today();
//        System.out.println("date = " + CalculateDate.addDateHour(date, -16));

        // System.out.println("CalculateDate.getDateDay(CalculateDate.today()) = "
        // + CalculateDate.getDateDay(CalculateDate.today()));

//    }
}
