/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.github.utils.date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtil {


    /**
     * 创建时间
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date date(int year, int month, int day, int hour, int minute, int second) {
        DateTime dateTime = new DateTime(year, month, day, hour, minute, second);
        return dateTime.toDate();
    }

    /**
     * 当前时间
     *
     * @return 当前时间 yyyy-MM-dd
     */
    public static String date() {
        DateTime dateTime = DateTime.now();
        return dateTime.toString("yyyy-MM-dd");
    }


    /**
     * 当前时间
     *
     * @param pattern 日期格式
     * @return
     */
    public static String date(String pattern) {
        DateTime dateTime = DateTime.now();
        return dateTime.toString(pattern);
    }


    /**
     * 当前年份
     *
     * @return 当前年份
     */
    public static int year() {
        DateTime dateTime = DateTime.now();
        return dateTime.getYear();
    }

    /**
     * 当前月份
     *
     * @return 当前月份
     */
    public static int month() {
        DateTime dateTime = DateTime.now();
        return dateTime.getMonthOfYear();
    }

    /**
     * 当前天数
     *
     * @return
     */
    public static int day() {
        DateTime dateTime = DateTime.now();
        return dateTime.getDayOfMonth();
    }

    /**
     * 当前星期
     *
     * @return 星期几
     */
    public static int week() {
        DateTime dateTime = DateTime.now();
        return dateTime.getDayOfWeek();
    }

    /**
     * 日期格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(pattern);
    }

    /**
     * 日期格式化
     *
     * @param timeMillis
     * @param pattern
     * @return
     */
    public static String formatDate(long timeMillis, String pattern) {
        DateTime dateTime = new DateTime(timeMillis);
        return dateTime.toString(pattern);
    }


    /**
     * 字符串转日期
     *
     * @param str
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date parseDate(String str, String pattern) {
        DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
        DateTime dateTime = DateTime.parse(str, format);
        return dateTime.toDate();
    }


    /**
     * 增加天数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date plusDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        DateTime dt = dateTime.plusDays(days);
        return dt.toDate();
    }

    /**
     * 减少天数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date minusDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        DateTime dt = dateTime.minusDays(days);
        return dt.toDate();
    }


    /**
     * 获取两个日期之间的天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int diffDays(Date start, Date end) {
        DateTime s = new DateTime(start);
        DateTime e = new DateTime(end);
        int days = Days.daysBetween(s, e).getDays();
        return days;
    }
}
