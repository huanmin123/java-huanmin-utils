package com.utils.common.multithreading.aqs.marriagephaser;

import com.utils.common.base.UniversalException;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class MarriagePhaserDemo {
    static MarriagePhaser phaser = new MarriagePhaser();
    static Random r = new Random();
    static void milliSleep(int milli){
        try {
            TimeUnit.MICROSECONDS.sleep(milli);
        } catch (InterruptedException e) {
             UniversalException.logError(e);
        }
    }
    
    
    //定义一个婚礼的阶段
    static class MarriagePhaser extends Phaser{
    
        /**
         * 重写这个方法,在每个阶段结束的时候会调用这个方法
         * @param phase         当前阶段,从0开始
         * @param registeredParties  当前进来的人数
         * @return
         */
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase){
                case 0 ://第一个阶段
                    System.out.println("所有人都到齐了！" + registeredParties);
                    System.out.println();
                    return false;
                case 1://第二个阶段
                    System.out.println("所有人都吃完了！" + registeredParties);
                    System.out.println();
                    return false;
                case 2://第三个阶段
                    System.out.println("所有人都离开了！" + registeredParties);
                    System.out.println();
                    return false;
                case 3://第四个阶段
                    System.out.println("婚礼结束！新郎新娘抱抱!" + registeredParties);
                    System.out.println();
                    return true;
                default:
                    return true;
            }
        }
    }
    
    static class Person implements Runnable{
        
        String name;
        
        public Person(String name) {
            this.name = name;
        }
        public void arrive(){
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 到达现场！\n", name);
            phaser.arriveAndAwaitAdvance(); //到达这个相位器并等待其他人到达
        }
        
        public void eat(){
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 吃完！\n", name);
            phaser.arriveAndAwaitAdvance();
        }
        
        public void leave(){
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 离开！\n", name);
            phaser.arriveAndAwaitAdvance();
        }
        
        public void hug(){
            if(name.equals("新郎") || name.equals("新娘")){
                milliSleep(r.nextInt(1000));
                System.out.printf("%s 洞房！\n", name);
                phaser.arriveAndAwaitAdvance();
            }else {
                phaser.arriveAndDeregister();
            }
        }
        
        
        @Override
        public void run() {
            arrive();//第一个阶段,到达现场
            eat(); //第二个阶段,吃饭,吃完
            leave();//第三个阶段,离开
            hug();//第四个阶段,洞房
        }
    }

    public static void main(String[] args) {
        //注册人数一共七个人  ,有5个嘉宾，加上新郎，新娘
        phaser.bulkRegister(7);
        //创建5个线程
        for (int i = 0; i < 5; i++) {
            new Thread(new Person("p" + i)).start();
        }
        //创建新郎和新娘
        new Thread(new Person("新郎")).start();
        new Thread(new Person("新娘")).start();
        
    }
}
