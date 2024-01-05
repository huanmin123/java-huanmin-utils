package com.utils.common.spring;

import org.springframework.core.env.Environment;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class SessionUtil {
    private static Environment environment = SpringContextHolder.getApplicationContext().getEnvironment();
    private static int time; //秒
    static {
        time = Integer.parseInt(Objects.requireNonNull(environment.getProperty("session.time")));
    }
    //默认session的时间是30分钟,
    public static void  setSession(String key ,Object value) {
        HttpSession session = ContextAttribuesUtils.getRequest().getSession();
        session.setAttribute(key,value);
        session.setMaxInactiveInterval(time);

    }
    public static <T> T  getSession(String key,Class<T> clazz ) {
        HttpSession session = ContextAttribuesUtils.getRequest().getSession();
        return (T)session.getAttribute(key);
    }
    public static void delSession(String key ) {
        HttpSession session = ContextAttribuesUtils.getRequest().getSession();
        session.removeAttribute(key);
    }
}
