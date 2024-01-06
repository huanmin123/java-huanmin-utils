package com.huanmin.test.utils_common.base;

import com.utils.common.base.SnowIdUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnowIdUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(SnowIdUtilsTest.class);
    @Test
    public void id(){
        long startTime = System.currentTimeMillis(); //获取开始时间
        for (int i = 0; i < 5000000; i++) {
//            System.out.println( SnowIdUtils.uniqueLong());  //long类型的雪花id
            long id= SnowIdUtil.uniqueLong();  //测试雪花id速度
//            System.out.println(SnowIdUtils.uniqueLongHex()); //Spring 类型的雪花id
        }
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间

    }
    @Test
    public  void slice(){
        //  雪花id   分片数3   结果只能是: 0 1  2
        long l = SnowIdUtil.uniqueLong();
        System.out.println("slice"+l);
        System.out.println("slice"+SnowIdUtil.slicePosition(l,3));
        System.out.println("slice"+SnowIdUtil.slicePosition(l,3));
    }
    @Test
    public  void tttt(){
        long id=SnowIdUtil.uniqueLong();
        String s = SnowIdUtil.uniqueLongHex();
        System.out.println(id);//8位
        System.out.println(s);
        System.out.println(s.length()); //16位

    }

}
