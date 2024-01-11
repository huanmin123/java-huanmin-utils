package com.huanmin.utils.common.multithreading.aqs.semaphore;

import java.lang.annotation.*;

@Documented

@Target({ElementType.METHOD})//作用:方法
@Retention(RetentionPolicy.RUNTIME)
public @interface SemaphoreDoc {
    String key(); //建议设置不然可能发生,不同方法重复限流现象
    int limit() default 3;

    int blockingTime() default 3;

}
