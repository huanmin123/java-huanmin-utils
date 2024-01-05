package com.utils.common.multithreading.cas;


import com.utils.common.multithreading.utils.SleepTools;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongUtils {
    //这样的设计是,为了便于多个线程之间好共享变量
    private static Map<String, AtomicLong> map=new ConcurrentHashMap<>();
    private  String key;  // key 对应的自增
    private  AtomicLong atomicLong;

    public AtomicLongUtils(String key) {
        this.key = key;
        this.atomicLong=initialize();
    }
    public static AtomicLongUtils build(String key) {
        if (StringUtils.isBlank(key)) {
            throw new  NullPointerException("不能为空");
        }
        return  new AtomicLongUtils(key);
    }

    private  AtomicLong initialize() {
        AtomicLong atomicLong = map.get(key);
        if (atomicLong==null) {
            synchronized (AtomicLongUtils.class) {
                atomicLong = map.get(key);
                if (atomicLong==null) {
                    AtomicLong atomicLong1 = new AtomicLong();
                    map.put(key,atomicLong1);
                    atomicLong=atomicLong1;
                }
            }

        }
        return  atomicLong;
    }

    /**
     * 自增++
     * @param
     */
    public  Long  incrementAdd() {

       return atomicLong.incrementAndGet();
    }
    //自减--
    public  Long  incrementSub() {
        return atomicLong.decrementAndGet();
    }


    /**
     * @param num 设置初始值(在启动线程前)   ,不能在多线程中使用,否则就会线程不安全
     */
    public  void  set(long num) {
        atomicLong.set(num);
    }

    /**
     * @param update   以原子的方式修改值 ,没有任何副作用(线程安全的) ,(有ABA问题)
     *
     */
    public  void  update(long update) {

        while (!atomicLong.compareAndSet(atomicLong.get(), update)) {
            SleepTools.ms(10);
        }

    }

    //返回当前的值,(最新的,因为底层使用volatile,保证了线程可见,获取的是最新的值)
    public  long  get() {
        return atomicLong.get();
    }




}
