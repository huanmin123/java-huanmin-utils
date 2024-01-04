package com.utils.common.base;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class DateUtil {
    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * Date转LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate DateToLocaleDate(Date date) {

        Instant instant = date.toInstant();

        ZoneId zoneId = ZoneId.systemDefault();

        return instant.atZone(zoneId).toLocalDate();

    }

    /**
     * Date转LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime DateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    //LocalDate转Date
    public static Date LocalDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
        return Date.from(instant);

    }

    //LocalDateTime转Date
    public static Date LocalDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);

    }


    /**
     * 计算2个日期之间相差天数
     *
     * @return
     */
    public static double diffInSec(Date startAt, Date endAt) {
        long milliseconds = endAt.getTime() - startAt.getTime();
        return milliseconds / 1000.0 / (60 * 60 * 24.0);
    }


    /**
     * 格式化日期,返回yyyy-MM-dd格式字符串
     *
     * @param date
     * @return
     */
    public static String dateToYmdString(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 格式化日期,返回yyyy-MM-dd HH:mm:ss格式字符串
     *
     * @param date
     * @return
     */
    public static String dateToYmdHmsString(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    //比较两个日期的大小
    public static boolean compareDate(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 对Date日期进行天数相加(days为负数时, 进行相减)
     *
     * @param date 日期
     * @param day  相加天数
     * @return java.util.Date
     */
    public static Date addDays(Date date, int day) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, day);
    }

}
