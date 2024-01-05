package com.utils.common.multithreading.cas;

import com.utils.common.multithreading.utils.SleepTools;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/7/29 15:08
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class AtomicBooleanUtils {
    //这样的设计是,为了便于多个线程之间好共享变量
    private static Map<String, AtomicBoolean> map=new ConcurrentHashMap<>();
    private  String key;  // key 对应的自增
    private  AtomicBoolean atomicBoolean;

    public AtomicBooleanUtils(String key) {
        this.key = key;
        this.atomicBoolean=initialize();
    }
    public static AtomicIntegerUtils build(String key) {
        if (StringUtils.isBlank(key)) {
            throw new  NullPointerException("不能为空");
        }
        return  new AtomicIntegerUtils(key);
    }

    private  AtomicBoolean initialize() {
        AtomicBoolean atomicBoolean = map.get(key);
        if (atomicBoolean==null) {
            synchronized (AtomicIntegerUtils.class) {
                atomicBoolean = map.get(key);
                if (atomicBoolean==null) {
                    AtomicBoolean atomicLong1 = new AtomicBoolean();
                    map.put(key,atomicLong1);
                    atomicBoolean=atomicLong1;
                }
            }
        }
        return  atomicBoolean;
    }


    /**
     * @param num 设置初始值(在启动线程前)   ,不能在多线程中使用,否则就会线程不安全
     */
    public  void  set(Boolean num) {
        atomicBoolean.set(num);
    }
    /**
     * @param update   以原子的方式修改值 ,没有任何副作用(线程安全的)  (有ABA问题)
     *
     */
    public  void  update(Boolean update) {
        while (!atomicBoolean.compareAndSet(atomicBoolean.get(),update)) {
            SleepTools.ms(10);
        }
    }

    //返回当前的值,(最新的,因为底层使用volatile,保证了线程可见,获取的是最新的值)
    public  Boolean  get() {
        return atomicBoolean.get();
    }



}
