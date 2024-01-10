package com.utils.common.obj.reflect;

import jdk.jfr.StackTrace;

import java.util.Arrays;
import java.util.Optional;

public class StackTraceUtil {
    //获取调用此方法的 类名 方法名,行号
    public static StackTraceElement currentStackTrace() {
        Optional<StackTraceElement> first = Arrays.stream(Thread.currentThread().getStackTrace()).skip(2).findFirst();
        return first.get();
    }


}
