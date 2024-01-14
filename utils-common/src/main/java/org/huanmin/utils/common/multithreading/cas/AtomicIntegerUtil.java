package org.huanmin.utils.common.multithreading.cas;


import org.huanmin.utils.common.multithreading.utils.SleepTools;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerUtil {

    private  AtomicInteger atomicInteger; //@Todo

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
