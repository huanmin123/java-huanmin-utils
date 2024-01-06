package com.utils.common.multithreading.cas;


import com.utils.common.multithreading.utils.SleepTools;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.function.Supplier;

// 防止Aba问题  版本戳为boolean类型。 不关心改了几次, 只关系改了没改, (在内存里如果将值改了,那么就可能绕过版本检查)
public class AtomicMarkableReferenceStringUtils {

    private AtomicMarkableReference<String> atomicMarkableReference; //@TODO


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
