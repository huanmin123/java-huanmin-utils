package com.huanmin.test.utils.utils_common.base;


import com.huanmin.utils.common.base.LocalDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class LocalDateUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(LocalDateUtilsTest.class);
    public static void main(String[] args) {
        //获取当前日期和时间字符串
        System.out.println(LocalDateUtil.getLocalDateTimeStr1());
        //获取当前日期字符串
        System.out.println(LocalDateUtil.getLocalDateStr());
        //获取当前时间字符串
        System.out.println(LocalDateUtil.getLocalTimeStr());
        //获取当前星期字符串
        System.out.println(LocalDateUtil.getDayOfWeekStr());
        //获取指定日期是星期几
        System.out.println(LocalDateUtil.getDayOfWeekStr(LocalDate.now()));

//       //获取日期时间字符串
//        System.out.println(LocalDateUtil.format(LocalDate.now(), UNSIGNED_DATE_PATTERN));


//         //T是合并时间的表示 遵守国际化时间合并规则
//        //日期时间字符串转换为日期时间
//        System.out.println(LocalDateUtil.parseLocalDateTime("2020-12-13 11:14:12", DATETIME_PATTERN));
//        //日期字符串转换为日期
//        System.out.println(LocalDateUtil.parseLocalDate("2020-12-13", DATE_PATTERN));



        //获取指定日期时间加上指定数量日期时间单位之后的日期时间
//        System.out.println(LocalDateUtil.plus(LocalDateTime.now(), 3, ChronoUnit.HOURS));
//        // 获取指定日期时间减去指定数量日期时间单位之后的日期时间.
//        System.out.println(LocalDateUtil.minus(LocalDateTime.now(), 4, ChronoUnit.DAYS));
//

//        //计算两个日期时间之间相隔日期时间    ChronoUnit.MINUTES 表示相隔多少分钟
//        System.out.println(LocalDateUtil.getChronoUnitBetween(LocalDateTime.now(), parseLocalDateTime("2020-12-12 12:03:12", DATETIME_PATTERN), ChronoUnit.MINUTES));
//        //计算两个日期之间相隔年数或月数或天数
//        System.out.println(LocalDateUtil.getChronoUnitBetween(LocalDate.now(), parseLocalDate("2021-12-12", DATE_PATTERN), ChronoUnit.WEEKS));

//        //获取本年第一天的日期字符串
//        System.out.println(getFirstDayOfYearStr());
//        //获取指定日期当年第一天的日期字符串
//        System.out.println(LocalDateUtil.getFirstDayOfYearStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN)));
//        //获取指定日期当年第一天的日期字符串,带日期格式化参数
//        System.out.println(LocalDateUtil.getFirstDayOfYearStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN), UNSIGNED_DATETIME_PATTERN));


//        //获取本年最后一天的日期字符串
//        System.out.println(LocalDateUtil.getLastDayOfYearStr());
//        //获取指定日期当年最后一天的日期字符串
//        System.out.println(LocalDateUtil.getLastDayOfYearStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN)));
//        //获取指定日期当年最后一天的日期字符串,带日期格式化参数
//        System.out.println(LocalDateUtil.getLastDayOfYearStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN), UNSIGNED_DATETIME_PATTERN));


//        //获取本月第一天的日期字符串
//        System.out.println(LocalDateUtil.getFirstDayOfMonthStr());
//        //获取指定日期当月第一天的日期字符串
//        System.out.println(LocalDateUtil.getFirstDayOfMonthStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN)));
//        //获取指定日期当月第一天的日期字符串,带日期格式化参数
//        System.out.println(LocalDateUtil.getFirstDayOfMonthStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN), UNSIGNED_DATETIME_PATTERN));



//        //获取本月最后一天的日期字符串
//        System.out.println(LocalDateUtil.getLastDayOfMonthStr());
//        //获取指定日期当月最后一天的日期字符串
//        System.out.println(LocalDateUtil.getLastDayOfMonthStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN)));
//        //获取指定日期当月最后一天的日期字符串,带日期格式化参
//        System.out.println(LocalDateUtil.getLastDayOfMonthStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN), UNSIGNED_DATETIME_PATTERN));


//        //获取本周第一天的日期字符串
//        System.out.println(LocalDateUtil.getFirstDayOfWeekStr());
//        //获取指定日期当周第一天的日期字符串,这里第一天为周一
//        System.out.println(LocalDateUtil.getFirstDayOfWeekStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN)));
//        //获取指定日期当周第一天的日期字符串,这里第一天为周一,带日期格式化参数
//        System.out.println(LocalDateUtil.getFirstDayOfWeekStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN), UNSIGNED_DATETIME_PATTERN));


//        //获取本周最后一天的日期字符串
//        System.out.println(LocalDateUtil.getLastDayOfWeekStr());
//        //获取指定日期当周最后一天的日期字符串,这里最后一天为周日
//        System.out.println(LocalDateUtil.getLastDayOfWeekStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN)));
//        //获取指定日期当周最后一天的日期字符串,这里最后一天为周日,带日期格式化参数
//        System.out.println(LocalDateUtil.getLastDayOfWeekStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN), UNSIGNED_DATETIME_PATTERN));


//        //获取今天开始时间的日期字符串
//        System.out.println(LocalDateUtil.getStartTimeOfDayStr());
//        //获取指定日期开始时间的日期字符串
//        System.out.println(LocalDateUtil.getStartTimeOfDayStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN)));
//        //获取指定日期开始时间的日期字符串,带日期格式化参数
//        System.out.println(LocalDateUtil.getStartTimeOfDayStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN), UNSIGNED_DATETIME_PATTERN));

//
//        //获取今天结束时间的日期字符串
//        System.out.println(LocalDateUtil.getEndTimeOfDayStr());
//        //获取指定日期结束时间的日期字符串
//        System.out.println(LocalDateUtil.getEndTimeOfDayStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN)));
//        //获取指定日期结束时间的日期字符串,带日期格式化参数
//        System.out.println(LocalDateUtil.getEndTimeOfDayStr(parseLocalDateTime("2021-12-12 12:03:12", DATETIME_PATTERN), UNSIGNED_DATETIME_PATTERN));


//         //切割日期。按照周期切割成小段日期段  年
//        List<String> dateStrs = LocalDateUtil.listDateStrs("2019-01-30", "2020-12-13", YEAR);
//        for (String dateStr : dateStrs) {
//            System.out.println(dateStr);
//        }


//        //割日期。按照周期切割成小段日期段  月
//        List<String> dateStrs1 = LocalDateUtil.listDateStrs("2019-01-30", "2020-12-13", MONTH);
//        for (String dateStr : dateStrs1) {
//            System.out.println(dateStr);
//        }

//         //割日期。按照周期切割成小段日期段日
//        List<String> dateStrs2 = LocalDateUtil.listDateStrs("2020-12-01", "2020-12-13", DAY);
//        for (String dateStr : dateStrs2) {
//            System.out.println(dateStr);
//        }
    }
}
