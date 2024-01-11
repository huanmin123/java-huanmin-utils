package com.huanmin.utils.server.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//通用接口返回值处理
@ControllerAdvice
@Slf4j
public class BaseResponseBodyHandleConfig implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        /*支持所有方法*/
        return true;
    }
    //所有接口返回信息都会从这里出去,我们可以在这里进行一些特殊处理,或者日志记录
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String methodName = returnType.getMethod().getName();
        log.info("{}方法返回值={}", methodName, JSON.toJSONString(body, SerializerFeature.WriteMapNullValue)); //WriteMapNullValue:输出空置字段
        return body;
    }




}
