package com.utils.common.base;


// 代码执行时间(毫秒)
public class CodeTimeUtil {
    // 1000毫秒=1秒
    public  static void  creator(Runnable runnable ) throws Exception {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        System.out.printf("执行时长：%d 毫秒.\n", (endTime - startTime) );

    }
    //微妙  (1000微妙=1毫秒)
    public  static void  creatorSubtle(Runnable runnable ) throws Exception {
        long startTime = System.nanoTime();
        runnable.run();
        long endTime = System.nanoTime();
        System.out.printf("执行时长：%d 微妙.\n", (endTime - startTime) );
    }

}
