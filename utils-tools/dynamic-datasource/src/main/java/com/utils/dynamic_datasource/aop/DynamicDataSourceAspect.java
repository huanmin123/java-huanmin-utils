package com.utils.dynamic_datasource.aop;

import com.utils.dynamic_datasource.base.DynamicDataSourceService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


/**
 * 动态数据源切换
 * @Description: 用于单独的请求或者类,包进行切换数据库
 */

@Aspect
@Component
public class DynamicDataSourceAspect {


    @Pointcut("@annotation(com.utils.dynamic_datasource.aop.DynamicDataSourceAnno)")
    public void dynamicDataSourceAnno() {

    }

    @Around("dynamicDataSourceAnno()")
    public Object DynamicDataSourceAspectAroundAnno(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            DynamicDataSourceAnno dynamicDataSourceAnno  = signature.getMethod().getAnnotation(DynamicDataSourceAnno.class);
            String key = dynamicDataSourceAnno.key();
            if (StringUtils.isNotBlank(key)) {
                //切换为指定数据库
                DynamicDataSourceService.switchDb(key);
            }
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }finally {
            //还原为默认配置
            DynamicDataSourceService.resetDb();
        }
        return object;
    }

    // 还可以扩展包路径切换
    @Pointcut("execution(* com.*.service.user.*.*(..))")
    public void dynamicDataSourceMethodService() {}

    @Around("dynamicDataSourceMethodService()")
    public Object dynamicDataSourceMethodService(ProceedingJoinPoint joinPoint) {
       return DynamicDataSourceAspectAroundAnno(joinPoint);
    }

}
