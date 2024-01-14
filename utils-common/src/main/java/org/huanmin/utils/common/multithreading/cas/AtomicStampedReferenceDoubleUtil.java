package org.huanmin.utils.common.multithreading.cas;


import org.huanmin.utils.common.multithreading.utils.SleepTools;

import java.util.concurrent.atomic.AtomicStampedReference;

// 防止Aba问题(版本号) 版本戳为int类型。 关心改了几次,比AtomicMarkableReference更加安全,防止在内存中把值改了的情况,因为版本是递增的
public class AtomicStampedReferenceDoubleUtil {

    private AtomicStampedReference<Double> atomicStampedReference; //@TODO



    /**
     * @param update   以原子的方式修改值 ,没有任何副作用(线程安全的) ,(有ABA问题)
     *
     */
    public  void  update(Double update) {
       combination(update);
    }


    //返回所有添加的和
    public Double get() {
        return atomicStampedReference.getReference();
    }

    private void combination(Double update) {

//        //当旧修版本号和上一次的版本号相同没有被修改 那么就将老值修改为新值
        int oldStamp, newOldStamp;
        Double oldReference,newReference;
        do {
            SleepTools.ms(10);
            oldStamp = atomicStampedReference.getStamp(); //获取版本号
            newOldStamp = oldStamp + 1; //新版本号
            oldReference = atomicStampedReference.getReference();//(老值)
            newReference = update; //自增CAS(新值)
            // 老值    新值        旧版本号           新版本号
        } while (!atomicStampedReference.compareAndSet(oldReference, newReference, oldStamp, newOldStamp));
    }

}