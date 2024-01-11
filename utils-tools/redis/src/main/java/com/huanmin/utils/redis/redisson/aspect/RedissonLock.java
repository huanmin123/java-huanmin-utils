package com.huanmin.utils.redis.redisson.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {

    //加锁的时间默认3秒,  如果任务在3秒内执行完毕那么自动释放锁,如果任务3秒内没有执行完毕也会释放锁, 所以内容执行时间过长适当加大锁的时间
    // 如果想要一直持有锁直到任务执行完毕,那么可以设置为-1
    int lockTime() default 3;
    String key() default "" ;  //唯一标识,如果没有那么默认为token->sessionId
    String doc() default "加锁失败,请稍后再试"; //加锁失败的提示信息
    int repeatLockCount() default 0; //重复加锁的次数 ,-1表示无限次数知道加锁成功,0表示不重复加锁,1表示加锁一次,2表示加锁两次
    int lockWaitTimeMs() default 100; //重复加锁默认的阻塞时间100毫秒,可以自己定义
}
