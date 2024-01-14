package org.huanmin.utils.server.config;


import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

//解决重写Controller 方法参数返回值不一致的问题,
@Configuration
public class WebMvcRegistrationsConfig implements WebMvcRegistrations {

    //解决重写Controller 方法参数返回值不一致的问题,
    //解决办法就是如果子类中有相同路径的url接口那么就不映射父类的url接口了
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping (){
                //handlerType.equals(ParentclassController.class) || handlerType.equals(SubclassController.class)
            @Override
            protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
                RequestMappingInfo methodMapping = super.getMappingForMethod(method, handlerType);
                if (methodMapping==null) {
                    return methodMapping;
                }

                Map<RequestMappingInfo, HandlerMethod> handlerMethods = super.getHandlerMethods();
                for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet()) {
                    for (String pattern : requestMappingInfoHandlerMethodEntry.getKey().getPatternsCondition().getPatterns()) {
                        for (String s : methodMapping.getPatternsCondition().getPatterns()) {
                            if (pattern.equals(s)) {    //发现有重复的
                                //删除原来的
                                super.unregisterMapping(requestMappingInfoHandlerMethodEntry.getKey());
                                return null;
                            }
                        }
                    }
                }

                return methodMapping;
            }
        };
    }
}