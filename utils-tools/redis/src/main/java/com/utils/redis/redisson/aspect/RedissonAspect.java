package com.utils.redis.redisson.aspect;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/8/1 17:37
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */

import com.utils.common.base.NullUtil;
import com.utils.common.spring.HttpJsonResponse;
import com.utils.redis.redisson.utils.RedissonLockUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

@Aspect
@Component
public class RedissonAspect {

    @Value("${spring.redis.redisson.tokenName}")
    private  String tokenName;

    @Autowired
    private RedissonLockUtil redisLock;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(RedissonLock noRepeatSubmit) {
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ra.getRequest();
    }

    @Around(value = "pointCut(noRepeatSubmit)", argNames = "pjp,noRepeatSubmit")
    public Object around(ProceedingJoinPoint pjp, RedissonLock noRepeatSubmit) throws Throwable {
        int lockSeconds = noRepeatSubmit.lockTime();
        String doc = noRepeatSubmit.doc();
        String keyName = noRepeatSubmit.key();
        int repeatLockCount = noRepeatSubmit.repeatLockCount();
        int lockWaitTimeMs = noRepeatSubmit.lockWaitTimeMs();

        HttpServletRequest request = getRequest();
        Assert.notNull(request, "request can not null");

        //如果没有唯一表示那么就使用token或者sessionID来唯一表示
        if(!NullUtil.notEmpty(keyName)){
            String token = request.getHeader(tokenName);
            if(NullUtil.notEmpty(token)){
                keyName=token;
            }else{
                //使用sessionID (注意保证分布式session共享)
                keyName = request.getSession().getId();
            }
            System.out.println("tokenName:"+keyName);
        }
        String path = request.getServletPath();
        String key = getKey(keyName, path);
        // 获取锁成功
        AtomicReference<Object> result=new AtomicReference<>();
        //加锁
        redisLock.stuntLock(key, lockSeconds,repeatLockCount,lockWaitTimeMs,()->{
            // 执行
            try {
                result.set(pjp.proceed());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        },()->{
            // 获取锁失败
            result.set(HttpJsonResponse.failed(doc));
        });

        return result;

    }

    private String getKey(String token, String path) {
        return token + path;
    }


}
