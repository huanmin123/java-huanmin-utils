package com.huanmin.utils.xss;



import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class XssFilterWrapper extends HttpServletRequestWrapper {

    public XssFilterWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 对参数处理 get  提交时候，防止站点脚本注入
     *
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return XSSUtil.cleanXSS(value, String.class);
    }

    /**
     * 对数值进行处理 get   post  提交时候 效验参数
     *
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapseValues[i] = XSSUtil.cleanXSS(values[i], String.class);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }


    //   请求头里过滤  提交时候 效验表单,防止站点脚本注入
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return XSSUtil.cleanXSS(value, String.class);
    }


    /**
     * 主要是针对HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE 获取pathvalue的时候把原来的pathvalue经过xss过滤掉
     */
    @Override
    public Object getAttribute(String name) {
        // 获取pathvalue的值
        if (HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE.equals(name)) {
            Map uriTemplateVars = (Map) super.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (Objects.isNull(uriTemplateVars)) {
                return uriTemplateVars;
            }
            Map newMap = new LinkedHashMap<>();
            uriTemplateVars.forEach((key, value) -> {
                if (value instanceof String) {
                    newMap.put(key, XSSUtil.cleanXSS((String) value, String.class));
                } else {
                    newMap.put(key, value);

                }
            });
            return newMap;
        } else {
            return super.getAttribute(name);
        }
    }

}
