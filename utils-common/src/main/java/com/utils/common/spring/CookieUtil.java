package com.utils.common.spring;


import org.springframework.core.env.Environment;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class CookieUtil {
    private static Environment environment = SpringContextHolder.getApplicationContext().getEnvironment();
    private static String domain;
    private static String path;
    private static int time;

    static {
        domain = Objects.requireNonNull(environment.getProperty("cookie.domain"));
        time = Integer.parseInt(Objects.requireNonNull(environment.getProperty("cookie.time")));
        path = Objects.requireNonNull(environment.getProperty("cookie.path"));

    }
    public static void  setCookie(String key,String value) {
        setCookie(key,value,true);
    }
    public static void  setCookie(String key,String value,boolean httpOnly) {
        Cookie cookie=new Cookie(key,value );
        cookie.setPath(path);
        cookie.setMaxAge(time);
        cookie.setDomain(domain);
        cookie.setHttpOnly(httpOnly);
        HttpServletResponse response = ContextAttribuesUtils.getResponse();
        response.addCookie(cookie);
    }

    public static String  getCookie(String key) {
        Cookie[] cookies = ContextAttribuesUtils.getRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return  cookie.getValue();
            }
        }
        return  null;
    }

    public static boolean  delCookie(String key) {
        Cookie[] cookies = ContextAttribuesUtils.getRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                cookie.setMaxAge(0);
                cookie.setPath(path);
                cookie.setDomain(domain);
                HttpServletResponse response = ContextAttribuesUtils.getResponse();
                response.addCookie(cookie);
                return   true;
            }
        }
        return  false;
    }


}
