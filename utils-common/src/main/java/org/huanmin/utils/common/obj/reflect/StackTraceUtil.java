package org.huanmin.utils.common.obj.reflect;

import java.util.Arrays;
import java.util.Optional;

public class StackTraceUtil {
    //获取调用此方法的 类名 方法名,行号
    public static StackTraceElement currentStackTrace() {
        Optional<StackTraceElement> first = Arrays.stream(Thread.currentThread().getStackTrace()).skip(2).findFirst();
        return first.get();
    }
    //类名.方法名(类名.java:行号)
    public static String currentStackTraceString(int num) {
        Optional<StackTraceElement> stackTrace = Arrays.stream(Thread.currentThread().getStackTrace()).skip(num).findFirst();
        StackTraceElement stackTraceElement = stackTrace.get();
        return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")";
    }



}
