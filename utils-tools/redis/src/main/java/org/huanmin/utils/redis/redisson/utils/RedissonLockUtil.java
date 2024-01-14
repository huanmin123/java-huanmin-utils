package org.huanmin.utils.redis.redisson.utils;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/8/1 17:39
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedissonLockUtil {

    //从配置类中获取redisson对象
    @Autowired
    private RedissonClient redissonClient;
    private final String LOCK_TITLE = "redisLock_";
    private final ThreadLocal<Integer> count = new ThreadLocal<>();//计数

    /**
     *  //一直等待获取锁
     * @param lockKey 锁的key
     * @param leaseTime 锁的过期时间,单位秒 ,意思就是说如果在leaseTime时间内没有释放锁,那么锁会自动释放
     * @param getLockHandle 获取锁后的处理
     */
    public void lock(String lockKey,long leaseTime,Runnable getLockHandle) {
        RLock lock = redissonClient.getLock((LOCK_TITLE + lockKey));
        try {
            lock.lock(leaseTime, TimeUnit.SECONDS);
            getLockHandle.run();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试获取锁,如果获取不到锁,那么就返回false,如果获取到锁,那么就返回true,一旦获取到锁,必须手动释放锁,不会自动释放
     * @param lockKey 锁的key
     * @param getLockHandle 获取锁后的处理
     * @param noLockHandle 未获取锁的处理
     */
    public void tryLock(String lockKey,Runnable getLockHandle,Runnable noLockHandle) {
        RLock lock = redissonClient.getLock((LOCK_TITLE + lockKey));
        boolean b = lock.tryLock();
        if (b) {
            try {
                System.out.println(lockKey+"获取锁成功");
                getLockHandle.run();
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("获取锁失败");
            noLockHandle.run();
        }
    }
    //加锁 , 线程加锁的时候发现,锁有人用了 ,那么就会进入自旋等待

    /**
     *
     * 多功能加锁,可以设置超时时间,设置是否自璇,可以设置自旋时间,可以设置自旋次数
     * @param lockKey 锁的key
     * @param seconds 锁的过期时间,单位秒 ,意思就是说如果在seconds时间内没有释放锁,那么锁会自动释放,-1表示永不过期
     * @param repeatLockCount 重复加锁的次数 ,-1表示无限次数直到获取锁成功,0表示不重复加锁,1表示加锁一次,2表示加锁两次
     * @param lockWaitTimeMs 每次加锁的等待时间 ,间隔时间
     * @param getLockHandle 获取锁的回调
     * @param noLockHandle  没有获取锁的回调
     */
    @SneakyThrows
    public void stuntLock(String lockKey, long seconds, int repeatLockCount, int lockWaitTimeMs
            ,Runnable getLockHandle,Runnable noLockHandle) {
        count.set(0); //初始化值
        //获取锁对象
        RLock mylock = redissonClient.getLock((LOCK_TITLE + lockKey));
        mylock.lock();
        do {
            //尝试加锁
            boolean b = mylock.tryLock(0, seconds, TimeUnit.SECONDS);;
            if (b) {
                try {
                    System.out.println(lockKey+"获取锁成功");
                    getLockHandle.run();
                    return;
                }finally {
                    mylock.unlock();
                }
            }
            System.out.println("尝试获取锁" + Thread.currentThread().getName());
            if (repeatLockCount!=0){
                Thread.sleep(lockWaitTimeMs);//等待一段时间,再次尝试获取锁
                //加锁次数增加
                count.set(count.get() + 1);
            }
            //获取加锁的次数,如果是-1那么持续加锁,如果满足加锁次数那么结束加锁 ,如果是0那么不加锁
        } while (repeatLockCount == -1 || count.get() < repeatLockCount);
        log.warn(Thread.currentThread().getName() + "尝试加锁失败已尝试了:" + count.get() + "次");
        noLockHandle.run();
    }

    //手动锁的释放
    public void release(String lockKey) {
        //获取所对象
        RLock mylock = redissonClient.getLock((LOCK_TITLE + lockKey));
        // 这里判断下当前key是否上锁，不然业务执行时间大于锁自动释放时间后，解锁报异常
        if (mylock.isLocked()&&mylock.isHeldByCurrentThread()) { // 是否还是锁定状态并且锁是当前线程的
                mylock.unlock(); // 释放锁
                System.out.println("解锁:" + lockKey);
        }

    }
}
