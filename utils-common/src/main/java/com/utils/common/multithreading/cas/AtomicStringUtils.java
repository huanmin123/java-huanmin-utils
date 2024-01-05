package com.utils.common.multithreading.cas;


import com.utils.common.multithreading.utils.SleepTools;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

//AtomicReference的方式是直接替换地址
public class AtomicStringUtils {
    //这样的设计是,为了便于多个线程之间好共享变量
    private static Map<String, AtomicReference<String>> map = new ConcurrentHashMap<>();
    private String key;  // key 对应的自增
    private AtomicReference<String> atomicReference;

    public AtomicStringUtils(String key) {
        this.key = key;
        this.atomicReference = initialize();
    }

    public static AtomicStringUtils build(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("不能为空");
        }
        return new AtomicStringUtils(key);
    }

    private AtomicReference<String> initialize() {
        AtomicReference<String> atomicReference = map.get(key);
        if (atomicReference == null) {
            synchronized (AtomicStringUtils.class) {
                atomicReference = map.get(key);
                if (atomicReference == null) {
                    AtomicReference<String> atomicReference1 = new AtomicReference<String>("");
                    map.put(key, atomicReference1);
                    atomicReference = atomicReference1;
                }
            }

        }
        return atomicReference;
    }

    /**
     *  以原子的方式修改值 ,没有任何副作用(线程安全的)  (有ABA问题)
     */
    public void updateAndGet(String newValue) {
        while (!atomicReference.compareAndSet(atomicReference.get(), newValue)) {
            SleepTools.ms(10);
        }
    }

    /**
     * @param str 设置初始值
     */
    public void set(String str) {
        atomicReference.set(str);
    }

    //返回所有添加的和
    public String get() {
        return atomicReference.get();
    }


}
