package com.huanmin.test.utils_common.multithreading.cas;

import com.multithreading.executor.ExecutorUtil;
import com.multithreading.executor.ThreadFactoryUtil;
import com.multithreading.utils.SleepTools;
import org.junit.Test;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/6/24 18:23
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class AtomicIntegerUtilsTest {


    // 测试多线程的下使用AtomicInteger可见性
    @Test
    public void show(){
        AtomicIntegerUtils a = AtomicIntegerUtils.build("a");

        for (int i = 0; i < 100; i++) {
            ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.TEST,()->{
                a.incrementAdd();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }

        while (a.get()<100){
            System.out.println(a.get());
        }


    }
    @Test
    public void show1(){
        AtomicIntegerUtils a = AtomicIntegerUtils.build("a");

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.TEST,()->{
                a.update(a.get()+ finalI);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }
        SleepTools.ms(1000);
        System.out.println(a.get()); //4950


    }
}
