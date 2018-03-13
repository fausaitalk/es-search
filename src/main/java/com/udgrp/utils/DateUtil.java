
package com.udgrp.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author kejw
 * @version V1.0
 * @Project ud
 * @Description TODO
 * @date 2017/10/19
 */
public class DateUtil implements Serializable {

    /**
     * 将日期格式化转成字符串 统一转成 yyyy-MM-dd HH mm ss格式
     *
     * @param date
     * @return yyyy-MM-dd HH mm ss
     */
    public static String dateToStr(Date date) {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * 将日期格式化转成字符串 统一转成 yyyy-MM-dd HH mm ss格式
     *
     * @param date
     * @return yyyy-MM-dd HH mm ss
     */
    public static String dateToStrYMD(Date date) {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 将日期格式化转成字符串 统一转成 yyyy-MM-dd HH mm ss格式
     *
     * @param date
     * @param format
     * @return yyyy-MM-dd HH mm ss
     */
    public static String dateToStrFormat(Date date, String format) {
        FastDateFormat dateFormat = FastDateFormat.getInstance(format);
        return dateFormat.format(date);
    }

    /**
     * 将字符串转成yyyy-MM-dd HH mm ss格式日期
     *
     * @param dateStr
     * @return Date
     */
    public static Date strToDate(String dateStr) {
        FastDateFormat sdf = null;
        Date date = null;
        if (dateStr.length() > 10) {
            if (dateStr.indexOf("T") > -1) {
                dateStr = dateStr.replace("T", " ");
            }
            sdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = FastDateFormat.getInstance("yyyy-MM-dd");
        }
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 将字符串转成yyyy-MM-dd格式日期
     *
     * @param dateStr
     * @return date
     */
    public static Date strToDateYMD(String dateStr) {
        Date date = null;
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将字符串转成yyyy-MM-dd格式日期
     *
     * @param dateStr
     * @param format
     * @return date
     */
    public static Date strToDateFormat(String dateStr, String format) {
        Date date = null;
        FastDateFormat dateFormat = FastDateFormat.getInstance(format);
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当天开始时间 即零点
     *
     * @param date
     * @return
     */
    public static String getBeginDay(String date) {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(strToDateYMD(date + " 00:00:00"));
    }

    /**
     * 获取当天结束时间 即23 59 59
     *
     * @param date
     * @return
     */
    public static String getEndDay(String date) {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(strToDate(date + " 23:59:59"));
    }

    /**
     * 返回日期的毫秒值
     *
     * @param date
     * @return String
     * @throws ParseException
     */
    public static long getTimeInMillis(String date) {
        if (StringUtils.isBlank(date)) {
            return 0L;
        }
        FastDateFormat dateformat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = Calendar.getInstance();
        try {
            c1.setTime(dateformat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c1.getTimeInMillis();
    }

    /**
     * 取昨天日期
     *
     * @return
     */
    public static Date yesterdayDate() {
        FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return strToDate(sdf.format(c.getTime()));
    }


    /**
     * 取今天日期
     *
     * @return String
     */
    public static String today() {
        FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

    /**
     * 取明天日期
     *
     * @return String
     */
    public static String tomorrowDate() {
        FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +1);
        return sdf.format(c.getTime());
    }


    /**
     * 取第n天日期
     *
     * @param day
     * @return
     */
    public static String getDayDate(Integer day) {
        FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, day);
        return sdf.format(c.getTime());
    }

    /**
     * 求两个日期相差天数
     *
     * @param d1
     * @param d2
     * @return long
     */
    public static int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) {
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                // 得到当年的实际天数
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 两个日期相隔天数
     *
     * @param startday
     * @param endday
     * @return int
     */
    public static int getIntervalDays(Date startday, Date endday) {
        if (startday.after(endday)) {
            Date cal = startday;
            startday = endday;
            endday = cal;
        }
        long sl = startday.getTime();
        long el = endday.getTime();
        long ei = el - sl;
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    /**
     * 返回两个日期相差的天数
     *
     * @param startDate
     * @param endDate
     * @return long
     * @throws ParseException
     */
    public static long dateDiff(Date startDate, Date endDate) {
        long totalDate = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        long timestart = calendar.getTimeInMillis();
        calendar.setTime(endDate);
        long timeend = calendar.getTimeInMillis();
        totalDate = (timeend - timestart) / (1000 * 60 * 60 * 24);
        return totalDate;
    }

    /**
     * 日期转换成日历格式
     *
     * @param date
     * @return Calendar
     */
    public static Calendar date2Calendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * 获取两个日期间的所有时间集合
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static List<String> getToDayDates(String beginDate, String endDate) throws ParseException {

        FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(beginDate));
        List<String> days = new ArrayList<>();
        for (long d = cal.getTimeInMillis(); d <= sdf.parse(endDate).getTime(); d = getTimeInMillis(cal)) {
            days.add(sdf.format(d));
        }
        return days;
    }

    private static long getTimeInMillis(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }
}
