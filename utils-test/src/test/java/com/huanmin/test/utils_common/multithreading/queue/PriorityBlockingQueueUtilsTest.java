package com.huanmin.test.utils_common.multithreading.queue;


import com.multithreading.queue.priority.PriorityBlockingQueueUtils;
import com.multithreading.queue.priority.PriorityEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriorityBlockingQueueUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(PriorityBlockingQueueUtilsTest.class);
    public static void main(String[] args) throws InterruptedException {
        PriorityBlockingQueueUtils build = PriorityBlockingQueueUtils.build("da1");

        build.add("hello,1", PriorityEnum.USER);
        build.add("hello,1",PriorityEnum.USER);
        build.add("hello,2",PriorityEnum.VIP_USER);
        build.add("hello,3",PriorityEnum.VIP_USER);
        build.add("hello,1",PriorityEnum.USER);
        build.add("hello,1",PriorityEnum.MAX_VIP_USER);
        build.add("hello,1",PriorityEnum.USER);


        build.iteration((obj)->{
            System.out.println(String.valueOf(obj));
            return false; //true  //跳出迭代
        },String.class);





    }


}
