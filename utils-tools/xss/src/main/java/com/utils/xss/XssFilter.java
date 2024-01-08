package com.utils.xss;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *   XSS攻击过滤器
 * @author huanmin
 *
 */
@Component
@Order(Integer.MAX_VALUE)
public class XssFilter implements Filter {

    // 忽略权限检查的url地址
    private final String[] excludeUrls = new String[]{
            "null"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        try {

            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String pathInfo = req.getPathInfo() == null ? "" : req.getPathInfo();
            //获取请求url的后两层
            String url = req.getServletPath() + pathInfo;
            //获取请求你ip后的全部路径
            String uri = req.getRequestURI();
            //注入xss过滤器实例
            HttpServletRequest httpServletRequest = new XssFilterWrapper((HttpServletRequest)servletRequest);

            //过滤掉不需要的Xss校验的地址
            for (String str : excludeUrls) {
                if (uri.contains(str)) { //排除不需要过滤的路径
                    filterChain.doFilter(servletRequest, response);
                    return;
                }
            }
            //xss过滤
            filterChain.doFilter(httpServletRequest, response);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @Override
    public void destroy() {}
}

