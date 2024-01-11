package com.huanmin.utils.common.multithreading.cas;


import com.huanmin.utils.common.multithreading.utils.SleepTools;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongUtil {

    private  AtomicLong atomicLong; //@TODO


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
