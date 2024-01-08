package com.huanmin.test.utils_tools.redis;


import com.huanmin.test.TestApplication;
import com.utils.redis.redisTemplate.utils.RedisTemplateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReidsApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(ReidsApplicationTests.class);
    @Autowired
    RedisTemplateUtil redisTemplateUtil;

    @Test
    public void testRedis() {
        boolean hu = redisTemplateUtil.set("userCount", 0,-1);//设置key 永不过期
        System.out.println(String.valueOf(hu));
    }
    @Test
    public void testRedis1() {
        Object hu1 = redisTemplateUtil.get("userCount"); //读取key值
        System.out.println((String) hu1);

    }

    @Test
    public void testRedis_() {
        Object hu1 = redisTemplateUtil.incr("userCount",1);//每次加1
        System.out.println((String) hu1);

    }
    @Test
    public void testRedis_1() {

        Object hu1 = redisTemplateUtil.decr("userCount",1);//每次减1
        System.out.println((String) hu1);
    }





    @Test
    public void testRedis2() {
        Object hu1 = redisTemplateUtil.hset("login","username","token",60); //4   4秒 , 604800   7天过期时间   3600 一小时
        System.out.println((String) hu1);

    }


    @Test
    public void testRedis3() {
        Object hu1 = redisTemplateUtil.hget("login","username"); //获取hash里的key对应的value
        System.out.println((String) hu1);

    }


    @Test
    public void testRedis4() {
        boolean hu1 = redisTemplateUtil.hHasKey("login","username"); //判断hash里的key是否存在,等同于判断是否过期
        System.out.println(String.valueOf(hu1));

    }


    @Test
    public void testRedis5() {
       long hu1 = redisTemplateUtil.getExpire("login"); //判断redis , key过期时间  -1 永久  -2不存在此key(过期了)  ,大于0剩下的过期时间(秒)
        System.out.println(String.valueOf(hu1));

    }




}
