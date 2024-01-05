package com.utils.common.multithreading.timer.spring;

import java.io.Serializable;
import java.lang.reflect.Method;

public class SpringTimerRunnable implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;
    private Class aClass;
    private String key;

    public SpringTimerRunnable(Class aClass, String key) {
        this.aClass = aClass;
        this.key = key;
    }

    @Override
    public void run() {
        try {
            Object o = aClass.newInstance();
            //1.先获取每个方法上的指定注解
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                try {
                    if (key.equals(aClass.getName() +"."+ method.getName())) {
                        method.invoke(o);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
