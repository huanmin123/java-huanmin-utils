package com.huanmin.test.utils.utils_common.multithreading.readwritelock;

import com.huanmin.utils.common.multithreading.readwritelock.ReadWriteLockUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadWriteLockUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(ReadWriteLockUtilTest.class);
    private  int num=0;

    @Test
    public void test1() throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ReadWriteLockUtil.getWriteLock("da1",()->{

                        num++;
                        System.out.println("x"+num);
                    });

                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    ReadWriteLockUtil.getReadLock("da1",()->{
                        System.out.println("r"+num);
                    });
                }
            }).start();
        }

        Thread.sleep(1000);
    }


}
