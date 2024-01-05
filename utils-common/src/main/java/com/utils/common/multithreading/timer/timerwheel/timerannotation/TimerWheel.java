package com.utils.common.multithreading.timer.timerwheel.timerannotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimerWheel {

    Distributed distributedLocks()default Distributed.NONE; //分布式锁 ,默认不使用分布式锁
}
