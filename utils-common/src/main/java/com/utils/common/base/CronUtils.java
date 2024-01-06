package com.utils.common.base;


import com.utils.common.enums.DateEnum;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Cron 表达式生成和解析
 */
public class CronUtils {
    private static final Logger logger = LoggerFactory.getLogger(CronUtils.class);
  // String cron = "0 0 1 * * ?";

    /**
     *
     * @param cron
     * @return  yyyyMMddHHmmss
     */
    public synchronized static String resolverCron( String cron)  {
        // 加载包之后直接引用这个方法
        CronExpression cronExpression = null;
        try {
            cronExpression = new CronExpression(cron);
        } catch (ParseException e) {
             UniversalException.logError(e);
        }

        // 转换成下次执行时间戳 是为了给最近一次执行时间一个初始时间，这里给当前时间
        Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(new Date());
        LocalDateTime localDateTime = LocalDateUtil.dateLocalDateTime(nextValidTimeAfter);
       ;
        return  LocalDateUtil.format(localDateTime, DateEnum.UNSIGNED_DATETIME_PATTERN.getValue());

    }

    public   static void   crinVerif(String cron) throws Exception {
        if (StringUtils.isBlank(cron)||!CronExpression.isValidExpression(cron)) {//判断cron合法性
            throw  new Exception("cron 错误,不合法");
        }
    }


    //判断当前时间和旧(old)的时间是否一致
    public  static boolean equalsTime(String old) {
        return  LocalDateUtil.format(LocalDateUtil.getLocalDateTime(), DateEnum.UNSIGNED_DATETIME_PATTERN.getValue()).equals(old);
    }


    public static void main(String[] args) {

        boolean validExpression = CronExpression.isValidExpression("1 * * * * ?");
        System.out.println("main"+validExpression);

        String s = resolverCron("1 * * * * ?");

        System.out.println( s);
        while (true) {
            boolean b = equalsTime(s);
            if (b) {
                System.out.println(s);
                return;
            }
        }

    }
}
