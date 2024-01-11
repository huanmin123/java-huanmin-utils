package com.huanmin.utils.common.multithreading.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

// 手动控制线程阻塞和唤醒 ,可指定唤醒线程
public class LockSupportUtil {
    //线程对象池 ,key必须唯一的不然唤醒的线程就不是想要的线程了,线程使用完毕及时删除对应的key,value
    // 线程池的线程在程序运行完毕后没有死亡,而是被线程池收回从重利用了,
    // 所以需要在线程内部自己手动去清除记录,不然到时候会出现老的线程任务将新的线程任务给覆盖了
    // 当然如果是长期持续使用的线程不需要删除
  private static Map<String, Thread> map=new ConcurrentHashMap<>();

  static {
      //启动自动清除死亡线程对象
      clearThread();
  }


    //创建一个守护线程来监听线程是否已死亡如果死亡那么就删除
    private   static  void clearThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                for (Map.Entry<String, Thread> stringThreadEntry : map.entrySet()) {
                    if (!stringThreadEntry.getValue().isAlive()) {
                        System.out.println("删除死亡线程:"+stringThreadEntry.getKey());
                        map.remove(stringThreadEntry.getKey());
                    }
                }
                //每一秒去看一次
                SleepTools.second(1);
            }
        });
        thread.setDaemon(true); //开启守护线程
        thread.start();
    }



    //需要在start前配置
    public static   void setThread(String key,Thread thread) {
        map.put(key,thread);
    }
    public static   void setThread(String key) {
        map.put(key,Thread.currentThread());
    }
    //在线程内部使用
    public  static Thread getThread(String key) {
        return map.get(key);
    }

    //注意事项:
    // LockSupport 的阻塞和唤醒和Object里的阻塞和唤醒互不影响 ,LockSupport的只能唤醒LockSupport阻塞的, 其他的基本都一样

    //阻塞当前线程
    public static  void waitLock() {
        LockSupport.park();
    }

    //唤醒指定线程
    public static  boolean notifyLock(String key) {
        Thread thread = map.get(key);
        if (thread==null) {
            //线程死亡或者执行结束
            return false;
        }
        LockSupport.unpark(thread);
        return true;
    }
    //唤醒指定线程,然后 阻塞当前线程
    public static  boolean notifyWaitLock(String key) {
        Thread thread = map.get(key);
        if (thread==null) {
            //线程死亡或者执行结束
            return false;
        }
        LockSupport.unpark(thread);
        waitLock();
        return true;
    }


    private static void cleanThread() {
      map.remove(Thread.currentThread().getName());
   }

    //自动装载和清除  (用于线程池里使用)
   public static  Runnable   run(String key,Runnable runnable) {
      return ()->{
          map.put(key,Thread.currentThread());
          runnable.run();
          cleanThread();
      } ;

   }

}
