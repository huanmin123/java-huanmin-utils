package com.utils.common.multithreading.cas;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * 高性能,累加器,多线程自增  原子性(不能保证ABA问题)
 */
public class LongAdderUtils {
    //这样的设计是,为了便于多个线程之间好共享变量
    private static Map<String, LongAdder> map=new ConcurrentHashMap<>();
    private  String key;  // key 对应的自增
    private  LongAdder longAdder;

    public LongAdderUtils(String key) {
        this.key = key;
        this.longAdder=initialize();

    }
    public static LongAdderUtils build(String key) {
        if (StringUtils.isBlank(key)) {
            throw new  NullPointerException("不能为空");
        }
        return  new LongAdderUtils(key);
    }

    private  LongAdder initialize() {
        LongAdder longAdder = map.get(key);
        if (longAdder==null) {
            synchronized (LongAdderUtils.class) {
                longAdder = map.get(key);
                if (longAdder == null) {
                    LongAdder longAdder1 = new LongAdder();
                    map.put(key,longAdder1);
                    longAdder=longAdder1;
                }
            }
        }
        return  longAdder;
    }

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
