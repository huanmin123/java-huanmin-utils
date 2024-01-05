package com.utils.common.multithreading.cas;


import com.utils.common.multithreading.utils.SleepTools;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.function.Supplier;

// 防止Aba问题  版本戳为boolean类型。 不关心改了几次, 只关系改了没改, (在内存里如果将值改了,那么就可能绕过版本检查)
public class AtomicMarkableReferenceStringUtils {
    //这样的设计是,为了便于多个线程之间好共享变量
    private static Map<String, AtomicMarkableReference<String>> map = new ConcurrentHashMap<>();
    private String key;  // key 对应的自增
    private AtomicMarkableReference<String> atomicMarkableReference;
    private AtomicStringUtils atomicStringUtils;

    public AtomicMarkableReferenceStringUtils(String key) {
        this.key = key;
        this.atomicMarkableReference = initialize();
        this.atomicStringUtils = AtomicStringUtils.build(key);
    }


    public static AtomicMarkableReferenceStringUtils build(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("不能为空");
        }
        return new AtomicMarkableReferenceStringUtils(key);
    }


    private AtomicMarkableReference<String> initialize() {
        AtomicMarkableReference<String> atomicMarkableReference = map.get(key);
        if (atomicMarkableReference == null) {
            synchronized (AtomicMarkableReferenceStringUtils.class) {
                atomicMarkableReference = map.get(key);
                if (atomicMarkableReference == null) {

                    // 初始值   ,初始版本号
                    AtomicMarkableReference<String> atomicMarkableReference1 = new AtomicMarkableReference<String>("", true);
                    map.put(key, atomicMarkableReference1);
                    atomicMarkableReference = atomicMarkableReference1;
                }
            }

        }
        return atomicMarkableReference;
    }

    /**
     * 修改 , 以原子的方式修改值 ,没有任何副作用(线程安全的) 并且防止ABA问题
     *
     * @param
     */
    public void update(String newValue) {
        combination(() -> newValue);
    }

    //返回所有添加的和
    public String get() {
        return atomicMarkableReference.getReference();
    }

    private void combination(Supplier<String> function) {

        //当旧修版本号和上一次的版本号相同没有被修改 那么就将老值修改为新值
        boolean oldStamp, newOldStamp;
        String oldReference,newReference;
        do {
            SleepTools.ms(10);
            oldStamp = atomicMarkableReference.isMarked(); //获取版本号
            newOldStamp = !oldStamp; //新版本号
            oldReference = atomicMarkableReference.getReference();//(老值)
            newReference = function.get(); //自增CAS(新值)
            // 老值    新值        旧版本号           新版本号
        } while (!atomicMarkableReference.compareAndSet(oldReference, newReference, oldStamp, newOldStamp));
    }

}
