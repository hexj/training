package com.taobao.cun.admin.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.common.lang.StringUtil;

/**
 * 所有与日期相关的操作
 */
public class DateUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd/";

    /**
     * 把日期型转换成字符串形式。
     * 
     * @param date 日期
     * @param dateFormat 日期格式，例如"yyyy/MM/dd"、"yyyy年MM月dd"
     * @return 日期字符串
     */
    public static String toLocaleString(Date date, String dateFormat) {
        if (date == null) {
            return "";
        }

        if (StringUtil.isBlank(dateFormat)) {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
        }

        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * 把日期型转换成"yyyy/MM/dd/"字符串形式。
     * 
     * @param date
     * @return 日期字符串
     */
    public static String toLocaleString(Date date) {
        return toLocaleString(date, null);
    }

    /**
     * 获得sysdate+hours后的时间
     * 
     * @param hours 提前或者推后的时间
     * @return sysdate+hours后的时间
     */
    public static Date getSysDate(int hours) {
        Calendar time = Calendar.getInstance();

        time.add(Calendar.HOUR, hours);

        return time.getTime();
    }

    /**
     * 方法说明:天数差
     * 
     * @param firstDate
     * @param lastDate
     */
    public static int getTimeIntervalDays(Date firstDate, Date lastDate) {
        long intervals = lastDate.getTime() - firstDate.getTime() + (60 * 1000);

        if (intervals > 0) {
            long daysd = intervals / (24 * 60 * 60 * 1000);

            return new Long(daysd).intValue();
        }

        return 0;
    }

    /**
     * 方法说明:小时差
     * 
     * @param firstDate
     * @param lastDate
     */
    public static int getTimeIntervalHours(Date firstDate, Date lastDate) {
        long intervals = lastDate.getTime() - firstDate.getTime() + (60 * 1000);

        if (intervals > 0) {
            long longHours = (intervals / (60 * 60 * 1000)) % 24;

            return new Long(longHours).intValue();
        }

        return 0;
    }

    /**
     * 方法说明:分钟差
     * 
     * @param firstDate
     * @param lastDate
     */
    public static int getTimeIntervalMins(Date firstDate, Date lastDate) {
        long intervals = lastDate.getTime() - firstDate.getTime() + (60 * 1000);

        if (intervals > 0) {
            long longMins = (intervals / (60 * 1000)) % 60;

            return new Long(longMins).intValue();
        }

        return 0;
    }

    /**
     * 方法说明:parse date
     * 
     * @param date
     * @param dateformat
     */
    public static Date parseDate(String date, String dateformat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);

        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 比较日期是否大于当前日期
     */
    public static boolean afterNow(Date date) {
        if (date == null) {
            return false;
        }

        Calendar nowCar = Calendar.getInstance();
        Calendar car = Calendar.getInstance();

        car.setTime(date);

        return car.after(nowCar);
    }

    /*
     * 查看是否早几天
     */
    public static boolean afterDays(Date date, int day) {
        if (date == null) {
            return false;
        }

        Calendar levelDay = Calendar.getInstance();
        Calendar createDay = Calendar.getInstance();

        createDay.setTime(date);
        createDay.add(Calendar.DATE, day);

        if (createDay.after(levelDay)) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 查看是否早几小时
     */
    public static boolean afterHours(Date date, int hours) {
        if (date == null) {
            return false;
        }

        Calendar levelDay = Calendar.getInstance();
        Calendar createDay = Calendar.getInstance();

        createDay.setTime(date);
        createDay.add(Calendar.HOUR, hours);

        if (createDay.after(levelDay)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取得系统当前日期
     */
    public static Date getCurrentTime() {
        return new Date();
    }

    /**
     * 返回多少时间的前的时间, seconds 可以是负的
     * 
     * @param when
     * @param seconds
     */
    public static Date addTime(Date when, int seconds) {
        Calendar c = Calendar.getInstance();

        c.setTime(when);
        c.add(Calendar.SECOND, seconds);

        return c.getTime();
    }
}
