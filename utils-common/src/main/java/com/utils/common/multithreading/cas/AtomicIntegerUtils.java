package com.utils.common.multithreading.cas;


import com.utils.common.multithreading.utils.SleepTools;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerUtils {
    //这样的设计是,为了便于多个线程之间好共享变量
    private static Map<String, AtomicInteger> map=new ConcurrentHashMap<>();
    private  String key;  // key 对应的自增
    private  AtomicInteger atomicInteger;

    public AtomicIntegerUtils(String key) {
        this.key = key;
        this.atomicInteger=initialize();
    }
    public static AtomicIntegerUtils build(String key) {
        if (StringUtils.isBlank(key)) {
            throw new  NullPointerException("不能为空");
        }
        return  new AtomicIntegerUtils(key);
    }

    private  AtomicInteger initialize() {
        AtomicInteger atomicInteger = map.get(key);
        if (atomicInteger==null) {
            synchronized (AtomicIntegerUtils.class) {
                atomicInteger = map.get(key);
                if (atomicInteger==null) {
                    AtomicInteger atomicLong1 = new AtomicInteger();
                    map.put(key,atomicLong1);
                    atomicInteger=atomicLong1;
                }
            }
        }
        return  atomicInteger;
    }

    /**
     * 自增++
     * @param
     */
    public  Integer  incrementAdd() {

       return atomicInteger.incrementAndGet();
    }
    //自减--
    public  Integer  incrementSub() {
        return atomicInteger.decrementAndGet();
    }


    /**
     * @param num 设置初始值(在启动线程前)   ,不能在多线程中使用,否则就会线程不安全
     */
    public  void  set(int num) {
        atomicInteger.set(num);
    }

    /**
     * @param update   以原子的方式修改值 ,没有任何副作用(线程安全的)  (有ABA问题)
     *
     */
    public  void  update(int update) {
        while (!atomicInteger.compareAndSet(atomicInteger.get(), update)) {
            SleepTools.ms(10);
        }
    }

    //返回当前的值,(最新的,因为底层使用volatile,保证了线程可见,获取的是最新的值)
    public  Integer  get() {
        return atomicInteger.get();
    }




}
