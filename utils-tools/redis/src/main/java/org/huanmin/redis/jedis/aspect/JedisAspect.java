package org.huanmin.redis.jedis.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.huanmin.redis.jedis.utils.JedisUtil;
import org.springframework.stereotype.Component;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/8/8 11:45
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
@Aspect
@Component
public class JedisAspect {

    @Pointcut("@annotation(jedisConfig)")
    public void pointCut(JedisConfig jedisConfig) {
    }

    @Around(value = "pointCut(jedisConfig)", argNames = "pjp,jedisConfig")
    public Object around(ProceedingJoinPoint pjp, JedisConfig jedisConfig) throws Throwable {
        int db = jedisConfig.db();
        JedisUtil.dbIndex(db); //设置当前数据源索引
        Object proceed =null;
        try {
            proceed = pjp.proceed();
        } finally {
            JedisUtil.cleanThreadLocal();//清除线程变量
        }
        return  proceed;
    }
}
