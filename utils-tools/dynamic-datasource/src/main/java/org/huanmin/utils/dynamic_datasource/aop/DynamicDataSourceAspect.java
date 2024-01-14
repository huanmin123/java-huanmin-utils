package org.huanmin.utils.dynamic_datasource.aop;

import org.huanmin.utils.common.base.UniversalException;
import org.huanmin.utils.dynamic_datasource.base.DynamicDataSourceService;
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


    @Pointcut("@annotation(org.huanmin.utils.dynamic_datasource.aop.DBSwitch)|| @within(org.huanmin.utils.dynamic_datasource.aop.DBSwitch)")
    public void dynamicDataSourceAnno() {
//        ((MethodInvocationProceedingJoinPoint) joinPoint).signature.getDeclaringType().getAnnotation(DBSwitch.class)
    }

    @Around("dynamicDataSourceAnno()")
    public Object dynamicDataSourceAspectAroundAnno(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            DBSwitch dbSwitch  = signature.getMethod().getAnnotation(DBSwitch.class);
            if (dbSwitch==null){
                dbSwitch= (DBSwitch) signature.getDeclaringType().getAnnotation(DBSwitch.class);
            }
            String key = dbSwitch.value();
            if (StringUtils.isNotBlank(key)) {
                //切换为指定数据库
                DynamicDataSourceService.switchDb(key);
            }
            object = joinPoint.proceed();
        } catch (Throwable e) {
             UniversalException.logError(e);
        }finally {
            //还原为默认配置
            DynamicDataSourceService.resetDb();
        }
        return object;
    }

    // 还可以扩展包路径切换
//    @Pointcut("execution(* com.*.*(..))")
    public void dynamicDataSourceMethodService() {}

//    @Around("dynamicDataSourceMethodService()")
    public Object dynamicDataSourceMethodService(ProceedingJoinPoint joinPoint) {
       return dynamicDataSourceAspectAroundAnno(joinPoint);
    }

}
