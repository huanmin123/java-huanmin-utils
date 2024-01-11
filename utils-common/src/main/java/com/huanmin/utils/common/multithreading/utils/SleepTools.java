package com.huanmin.utils.common.multithreading.utils;

import java.util.concurrent.TimeUnit;

/**
 *@author dengcs
 *
 *类说明：线程休眠辅助工具类
 */
public class SleepTools {

    /**
     * 按秒休眠
     * @param seconds 秒数
     */
    public static  void second(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException exception) {
            exception.printStackTrace();

        }
    }

    /**
     * 按毫秒
     * 数休眠
     * @param seconds 毫秒数
     */
    public static  void ms(int seconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(seconds);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}

