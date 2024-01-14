package org.huanmin.utils.common.multithreading.cas;

import java.util.concurrent.atomic.LongAdder;

/**
 * 高性能,累加器,多线程自增  原子性(不能保证ABA问题)
 */
public class LongAdderUtil {
    private  LongAdder longAdder; //@TODO

    /**
     * 自增
     * @param
     */
    public  void  increment() {
        longAdder.increment();
    }
    /**
     * 指定每次加几
     * @param num
     */
    public  void  add(long num) {
        longAdder.add(num);
    }
    //返回所有添加的和
    public  long  get() {
       return longAdder.longValue();
    }


    //重置为0   相当于sum()后再调用reset()
    public  void  reset() {
        longAdder.sumThenReset();
    }



}
