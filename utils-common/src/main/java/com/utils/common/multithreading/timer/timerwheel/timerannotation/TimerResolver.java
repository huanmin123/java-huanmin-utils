package com.utils.common.multithreading.timer.timerwheel.timerannotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimerResolver {
    String resolver();
}
