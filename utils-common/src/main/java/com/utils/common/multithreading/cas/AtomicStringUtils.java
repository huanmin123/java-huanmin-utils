package com.utils.common.multithreading.cas;


import com.utils.common.multithreading.utils.SleepTools;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

//AtomicReference的方式是直接替换地址
public class AtomicStringUtils {

    private AtomicReference<String> atomicReference; //@TODO


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
