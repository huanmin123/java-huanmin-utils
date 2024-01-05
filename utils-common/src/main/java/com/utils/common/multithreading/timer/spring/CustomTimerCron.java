package com.utils.common.multithreading.timer.spring;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomTimerCron {

    String startTime()default ""; //启动时间 startTime时候,开始执行一次,然后每隔time执行一次   ""==null
    String  cron(); //cron表达式 (周期执行)
    int  count() default -1; //执行次数  -1==null
}
