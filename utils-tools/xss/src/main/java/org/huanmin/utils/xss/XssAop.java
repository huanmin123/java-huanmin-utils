package org.huanmin.utils.xss;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;

@Component
@Aspect
public class XssAop {
    //这里需要注意了，这个是将自己自定义注解作为切点的根据，路径一定要写正确了
    @Pointcut(value = "@annotation(org.huanmin.utils.xss.Xss) || @within(org.huanmin.utils.xss.Xss)") //拦截类和方法
    public void xss() {
    }

    //JoinPoint joinPoint
    @Before("xss()")
    public void xssAfter() throws IOException {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();


        // 获取请求头
        Enumeration<String> enumeration = request.getHeaderNames();
        StringBuffer headers = new StringBuffer();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            headers.append(name + ":" + value).append(",");
        }
        XSSUtil.cleanXSS(headers.toString(), String.class);
        //获取url 的参数
        StringBuffer params = new StringBuffer();
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            params.append(URLEncoder.encode(queryString, "UTF-8")).append(",");
        }
        XSSUtil.cleanXSS(params.toString(), String.class);
        //获取post Body 数据
        String body = getRequestBodyData(request);
        XSSUtil.cleanXSS(body, String.class);

    }
    //获取请求体body的内容
    public static String getRequestBodyData(HttpServletRequest request) throws IOException {
        BufferedReader bufferReader = new BufferedReader(request.getReader());
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = bufferReader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }


}
