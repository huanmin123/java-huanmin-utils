package com.huanmin.utils.common.multithreading.cas;

import com.huanmin.utils.common.multithreading.utils.SleepTools;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/7/29 15:08
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class AtomicBooleanUtil {

    private  AtomicBoolean atomicBoolean;

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
