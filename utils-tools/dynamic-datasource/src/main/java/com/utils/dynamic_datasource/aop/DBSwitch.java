package com.utils.dynamic_datasource.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})//作用:方法和类
@Retention(RetentionPolicy.RUNTIME)
public @interface DBSwitch {
    String value() default ""; //数据源的key

}
