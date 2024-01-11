package com.huanmin.utils.common.spring;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContextAttribuesUtils {


    public static HttpServletRequest  getRequest(){
        //请求
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return  request;
    }


    public static HttpServletResponse  getResponse(){
        //请求
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //响应
        HttpServletResponse response = ((ServletRequestAttributes)requestAttributes).getResponse();
        return  response;
    }

}
