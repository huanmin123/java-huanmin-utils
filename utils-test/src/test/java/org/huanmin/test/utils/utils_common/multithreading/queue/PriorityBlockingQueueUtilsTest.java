package org.huanmin.test.utils.utils_common.multithreading.queue;


import org.huanmin.utils.common.multithreading.queue.priority.PriorityBlockingQueueUtil;
import org.huanmin.utils.common.multithreading.queue.priority.PriorityEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriorityBlockingQueueUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(PriorityBlockingQueueUtilsTest.class);
    public static void main(String[] args) throws InterruptedException {
        PriorityBlockingQueueUtil<String> build = new   PriorityBlockingQueueUtil<>();
        build.add("hello,1", PriorityEnum.USER);
        build.add("hello,1",PriorityEnum.USER);
        build.add("hello,2",PriorityEnum.VIP_USER);
        build.add("hello,3",PriorityEnum.VIP_USER);
        build.add("hello,1",PriorityEnum.USER);
        build.add("hello,1",PriorityEnum.MAX_VIP_USER);
        build.add("hello,1",PriorityEnum.USER);

        while (build.isEmpty()) {
            String dataTake = (String) build.getDataTake();
            logger.info("dataTake:{}",dataTake);
            Thread.sleep(1000);
        }

    }


}
