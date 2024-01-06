package com.huanmin.test.utils_common.multithreading.queue;

import com.utils.common.multithreading.queue.linked.LinkedBlockingQueueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedBlockingQueueUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(LinkedBlockingQueueUtilsTest.class);
    public static void main(String[] args) {
        LinkedBlockingQueueUtil build = LinkedBlockingQueueUtil.build("da1");

        build.add(1);
        build.add(2);
        build.add(3);

//        build.iteration((data)->{
//            System.out.println(data);
//            return  false;
//        },Integer.class);

//        System.out.println(build.getData(Integer.class));

        String dataHandle = build.getDataHandle((data) -> {
            return "hello:" + data;
        }, String.class);
        System.out.println(dataHandle);
    }
}
