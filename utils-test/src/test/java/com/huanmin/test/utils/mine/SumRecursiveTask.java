package com.huanmin.test.utils.mine;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

//配置RecursiveTask，返回值为Long
public class SumRecursiveTask extends RecursiveTask<Long> {

    //大于3000要拆分(创建一个变量)
    //是否要拆分的临界值
    private static final long THRESHOLD = 3000L;

    //起始值
    private final long start;
    //结束值
    private final long end;

    //构造方法(传递起始值、结束值)
    public SumRecursiveTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    //任务编写完成
    @Override
    protected Long compute() {
        long length = end - start;
        //计算
        if(length < THRESHOLD){
            long sum = 0;
            for (long i = start; i <= end; i++) {
                sum +=i;
            }
            return sum;
        }else{
            //拆分
            long middle = (start + end) /2;
            SumRecursiveTask left = new SumRecursiveTask(start,middle);//从小到大
            left.fork();
            SumRecursiveTask right = new SumRecursiveTask(middle+1,end);//从大到小
            right.fork();
            return left.join() +right.join();
        }
    }

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        //放入线程池
        ForkJoinPool pool = new ForkJoinPool();
        SumRecursiveTask task = new SumRecursiveTask(1, 59999999999L);
        Long result = pool.invoke(task);
        System.out.println("result="+result); //结果为负数,因为超出了long的最大值了   ,平均消耗时间:4秒
        Long end = System.currentTimeMillis();
        System.out.println("消耗时间:"+(end-start));
    }

}