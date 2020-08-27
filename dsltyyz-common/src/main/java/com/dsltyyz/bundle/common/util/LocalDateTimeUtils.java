package com.dsltyyz.bundle.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Description:
 * LocalDateTime工具类
 *
 * @author: dsltyyz
 * @date: 2019/11/06
 */
public class LocalDateTimeUtils {

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
    public static String format(LocalDateTime date) {
        return format(date, PATTERN_DATETIME);
    }

    /**
     * 日期转字符串格式化输出
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(LocalDateTime date, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(dateTimeFormatter);
    }

    /**
     * 字符串转日期格式化输出
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static LocalDateTime parse(String dateString) {
        if (dateString.matches(REGEX_DATETIME)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_DATETIME);
            return LocalDateTime.parse(dateString, dateTimeFormatter);
        } else if (dateString.matches(REGEX_DATE)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_DATE);
            return LocalDateTime.of(LocalDate.parse(dateString, dateTimeFormatter), LocalTime.of(0, 0, 0));
        } else {
            throw new IllegalArgumentException("日期格式不正确");
        }
    }

}
