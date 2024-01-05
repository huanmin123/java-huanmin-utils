package com.utils.common.multithreading.readwritelock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockUtil {

    //创建读写锁池 (线程安全)
  private static   Map<String , ReadWriteLock> map=new ConcurrentHashMap<>();


    //获取读锁

    /**
     *
     * @param key  锁的唯一标识
     * @param  runnable 执行函数
     */
    public static void getReadLock(String key,Runnable runnable) {
        ReadWriteLock lock = getLock(key);
        Lock lock1 = lock.readLock();
        lock1.lock();
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
        }

    }
    /**
     *
     * @param key  锁的唯一标识
     * @param  runnable 执行函数
     */
    public static void getWriteLock(String key,Runnable runnable) {

        ReadWriteLock lock = getLock(key);
        Lock lock1 = lock.writeLock();
        lock1.lock();
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
        }
    }


    //拿到锁
    private static  ReadWriteLock getLock(String key) {
        ReadWriteLock readWriteLock = map.get(key);
        if (readWriteLock==null) {
            synchronized (ReadWriteLockUtil.class) {
                readWriteLock = map.get(key);
                if (readWriteLock==null) {
                    //创建读写锁,(非公平锁)
                    ReadWriteLock lock = new ReentrantReadWriteLock();
                    map.put(key,lock);
                    readWriteLock=lock;
                }
            }
        }
        return  readWriteLock;

    }

}
