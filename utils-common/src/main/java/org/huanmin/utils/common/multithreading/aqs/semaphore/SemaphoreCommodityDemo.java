package org.huanmin.utils.common.multithreading.aqs.semaphore;

import org.huanmin.utils.common.base.UniversalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀防止商品超卖现象案例
 */
public class SemaphoreCommodityDemo {
    private static final Logger logger = LoggerFactory.getLogger(SemaphoreCommodityDemo.class);
    //商品池
  private   Map<String, Semaphore> map=new ConcurrentHashMap<>();

    //初始化商品池
    public SemaphoreCommodityDemo() {
        //手机10部
        map.put("phone",new Semaphore(10));
        //电脑4台
        map.put("computer",new Semaphore(4));
    }

    /**
     *
     * @param name 商品名称
     * @return 购买是否成功
     */
    public boolean getbuy(String name) throws Exception {
        Semaphore semaphore = map.get(name);
        if(semaphore==null){
            return false;//商品不存在
        }
        while (true) {
            int availablePermit = semaphore.availablePermits();
            if (availablePermit==0) {
                //商品售空 ,清空商品
                map.remove(name);
                return  false;
            }
            boolean b = semaphore.tryAcquire(1, TimeUnit.SECONDS);
            if (b) {
                System.out.println("抢到商品了");
                //处理下单逻辑
                
                //如果下单失败释放令牌
//                semaphore.release();
                
                return  true;
            }

        }

    }

    public static void main(String[] args) throws Exception {
        SemaphoreCommodityDemo semaphoreCommodity=new SemaphoreCommodityDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    System.out.println(semaphoreCommodity.getbuy("computer"));
                } catch (Exception e) {
                     UniversalException.logError(e);
                }
            }).start();
        }


    }



}
