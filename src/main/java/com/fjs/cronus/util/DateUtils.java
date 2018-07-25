package com.fjs.cronus.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类 默认使用 "yyyy-MM-dd HH:mm:ss" 格式化日期
 *
 */
public final class DateUtils {
    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";

    public static String FORMAT_SHORT2 = "yyyyMMdd";
    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
    /**
     * 中文简写 如：2010年12月01日
     */
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd";
    /**
     * 中文全称 如：2010年12月01日 23时15分06秒
     */
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
    public static String FORMAT_FULL_Long = "yyyyMMddHHmmss";
    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return FORMAT_LONG;
    }

    /**
     * 根据预设格式返回当前日期
     *
     * @return
     */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 根据用户格式返回当前日期
     *
     * @param format
     * @return
     */
    public static String getNow(String format) {
        return format(new Date(), format);
    }

    /**
     * 使用预设格式格式化日期
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, getDatePattern());
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date
     *            日期
     * @param pattern
     *            日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate
     *            日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date
     *            日期
     * @param n
     *            要增加的月数
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     *
     * @param date
     *            日期
     * @param n
     *            要增加的天数
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 获取日期年份
     *
     * @param date
     *            日期
     * @return
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    public static Integer getYear2(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取日期月份
     *
     * @param date
     *            日期
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期日
     *
     * @param date
     *            日期
     * @return
     */
    public static Integer getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date
     *            日期字符串
     * @return
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date
     *            日期字符串
     * @param format
     *            日期格式
     * @return
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    public static int getHour(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能描述：返回分
     *
     * @param date
     *            日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }
    public static int dayForWeek(Date date){
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int dayForWeek = 0;
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            return dayForWeek;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static Date Str2Date(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    //计算两个时间相差的秒数
    public static long comparDateInSecond(Date a, Date b) {
        long n = 0;
        //获取各自的时间戳

        return n;
    }


    /**
     * 获取当前的年月
     *
     * @return
     */
    public static String getyyyyMMForThisMonth() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String str = "";
        String monthStr = "";
        if (month < 10) {
            monthStr = "0" + String.valueOf(month);
        } else {
            monthStr = String.valueOf(month);
        }
        return String.valueOf(year) + monthStr;
    }


    /**
     * 获取当前月的开始时间
     *
     * @return
     */
    public static Date getStartTimeOfThisMonth() {
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.DAY_OF_MONTH, begin.getActualMinimum(Calendar.DAY_OF_MONTH));
        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        Date beginDate = begin.getTime();
        System.out.println(beginDate);
        return beginDate;
    }

    /**
     * 获取下个月的开始时间(当前月的结束时间)
     *
     * @return
     */
    public static Date getStartTimeOfNextMonth() {
        Calendar end = Calendar.getInstance();
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        Date endDate = end.getTime();
        return endDate;
    }

    /**
     * 获取给定的月份的起始时间
     * @param yyyyMM
     * @return
     * @throws Exception
     */
    public static Date getBeginDateByStr(String yyyyMM) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        date = sdf.parse(yyyyMM);
        return date;
    }

    /**
     * 获取给定的月份的结束时间
     * @param yyyyMM
     * @return
     * @throws Exception
     */
    public static Date getEndDateByStr(String yyyyMM) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        date = sdf.parse(yyyyMM);
        int year = Integer.valueOf(StringUtils.substring(yyyyMM,0,4));
        int month = Integer.valueOf(StringUtils.substring(yyyyMM,4,6));
        if ( 12 == month) {
            year++;
            month = 01;
        } else {
            month ++;
        }
        String time = year+""+month;
        date = sdf.parse(time);
        return date;
    }

    /**
     * 获取当前日期是星期几
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 时间戳转化为时间
     * @param
     */
    public static String getDateString(Integer time){
        String d = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(FORMAT_LONG);
            long result =  Long.parseLong(time.toString());
            d = df.format(result*1000);
            return d;
        }catch (Exception e){
            e.printStackTrace();
        }
        return d;
    }

    public static Date getTodayStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getTodayEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /**
     * 获取指定时间所在周的周一 示例：2013-05-13 00:00:00.
     */
    public static Date getWeekStart(Date date) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return currentDate.getTime();
    }

    /**
     * 获取指定时间所在周的周日 示例：2013-05-19 23:59:59.
     */
    public static Date getWeekEnd(Date date) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(date);
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return currentDate.getTime();
    }

    /**
     * 根据某日期获取当前月第一天日期 示例：2016-12-01 00:00:00.
     */
    public static Date getMonthStart(Date cur) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 根据某日期获取当前月最后一天日期 示例：2016-12-31 23:59:59.
     */
    public static Date getMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static void main(String [] args){
        try {

//            Date myDate2 = parse("2018-05-21 00:00:00",FORMAT_LONG);
//
            Date date2 = new Date();
//
//            if (DateUtils.getTodayStartTime().compareTo(myDate2) == 1)
//            {
//                System.out.println("ddddd");
//            }
            int a = getHour(date2);
//            Date date2= new Date("20180101");
            String date = DateUtils.format(new Date(),DateUtils.FORMAT_SHORT2);
//            String date2 = DateUtils.format(DateUtils.addDay(new Date(),-8),DateUtils.FORMAT_SHORT);
            System.out.println(a);
//            System.out.println(date2+" 00:00:00");
        /* *//*   DateUtils  dateUtil = new DateUtils();*//*
            int gethour = getHour(new Date());
            int minute = getMinute(new Date());
            int week = dayForWeek(new Date());
            System.out.println(gethour);
            System.out.println(minute);
            System.out.println(week);*/
//            Date n = getTodayStartTime();
//            String today = DateUtils.format(n,DateUtils.FORMAT_LONG);
//            Date m = getTodayEndTime();
//            String tom = DateUtils.format(m,DateUtils.FORMAT_LONG);
//            String d = getDateString(1515035116);
//            System.out.println(d);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}