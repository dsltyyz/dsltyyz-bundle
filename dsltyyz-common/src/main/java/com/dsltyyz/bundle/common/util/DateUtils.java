package com.dsltyyz.bundle.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Description:
 * 日期工具类
 *
 * @author: dsltyyz
 * @date: 2019/11/06
 */
public class DateUtils {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    private static final String REGEX_DATETIME = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
    private static final String REGEX_DATE = "^\\d{4}-\\d{2}-\\d{2}$";

    /**
     * 日期转字符串格式化输出
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, PATTERN_DATETIME);
    }

    /**
     * 日期转字符串格式化输出
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 字符串转日期格式化输出
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateString) throws ParseException {
        if (dateString.matches(REGEX_DATETIME)) {
            return parse(dateString, PATTERN_DATETIME);
        } else if (dateString.matches(REGEX_DATE)) {
            return parse(dateString, PATTERN_DATE);
        } else {
            throw new IllegalArgumentException("日期格式不正确 默认只支持：yyyy-MM-dd HH:mm:ss和yyyy-MM-dd");
        }
    }

    /**
     * 字符串转日期格式化输出
     *
     * @param dateString
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateString, String pattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(dateString);
    }

    /**
     * 计算时间
     *
     * @param date 当前日期
     * @param calendarEnum 添加位置 Calendar
     * @param i 数目
     * @return
     */
    public static Date calc(Date date, int calendarEnum, int i) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(calendarEnum, i);
        return gc.getTime();
    }
}
