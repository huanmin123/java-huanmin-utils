package org.huanmin.test.utils.utils_common.multithreading.executor;

import org.huanmin.utils.common.base.UniversalException;
import org.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import org.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/8/10 16:55
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class ExecutorTest {


    @Test
    public  void show(){

        List<Callable> callables=new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            callables.add(()->{
//                throw  new Exception("---");
                int i1 = new Random().nextInt(100);
                Thread.sleep(i1 );
                System.out.println("睡眠:"+i1+"返回结果:"+finalI);
                return finalI;
            });
        }
//        Collections.shuffle(callables);

        ExecutorUtil.createCompletionServicesAll(ThreadFactoryUtil.ThreadConfig.TEST,callables,(o)->{
            try {
                System.out.println(o.get()); //如果某个线程出现异常则抛出异常我们这里可以捕捉到
            } catch (InterruptedException | ExecutionException e) {
                 UniversalException.logError(e);
            }
        });
    }

    @Test
    public  void show1(){

        List<Callable> callables=new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            callables.add(()->{
//                throw  new Exception("---");
                int i1 = new Random().nextInt(1000);
                Thread.sleep(i1);
                System.out.println("睡眠:"+i1+"返回结果:"+finalI);
                return finalI;
            });
        }

        try {
            //获取第一个执行完毕的结果
            Integer test = (Integer) ExecutorUtil.createCompletionServicesOne(ThreadFactoryUtil.ThreadConfig.TEST, callables);
            System.out.println("result:"+test);
        } catch (ExecutionException e) {
             UniversalException.logError(e);
        }


    }
}
