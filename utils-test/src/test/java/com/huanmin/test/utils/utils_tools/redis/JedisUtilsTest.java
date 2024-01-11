package com.huanmin.test.utils.utils_tools.redis;

import com.huanmin.test.TestApplication;

import com.huanmin.utils.common.multithreading.utils.SleepTools;
import com.huanmin.utils.redis.jedis.aspect.JedisConfig;
import com.huanmin.utils.redis.jedis.utils.JedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/8/4 12:45
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JedisUtilsTest {
    @Autowired
    private JedisUtil jedisUtils;
    @Test
    public   void testJedis() {
        jedisUtils.redisStr.set("test","1");
    }

    @Test
    public   void map() {
        final Long hset = jedisUtils.redisMap.hset("testmap", "da1", "1");
        System.out.println(hset);
    }


    @Test
    public   void subscribe() {
        jedisUtils.publishSubscription.subscribe((mess)->{
            System.out.println(mess);
        },"test");
        //然后在redis 客户端 使用PUBLISH test "123" 发送消息
    }
    @Test
    public  void redisManage(){
        List<Object> manage1 = jedisUtils.redisManage.manage((t) -> {
            t.set("manage", "1");
            t.set("manage", "2");
            t.get("manage");
        });
        System.out.println(manage1);
    }
    @Test
    public  void redisManageCAS(){
        List<Object> manage1 = jedisUtils.redisManage.manageCAS((t) -> {
            t.set("manage", "1");
            t.set("manage", "2");
            t.get("manage");
            SleepTools.second(10);
        },"manage");
        System.out.println(manage1);
    }

    @Test
    public  void redisLua(){
        ArrayList<String> keys = new ArrayList<>();
        keys.add("key1");
        keys.add("key2");
        ArrayList<String> args = new ArrayList<>();
        args.add("args1");
        args.add("args2");
        List<String> eval= (List<String>)jedisUtils.redisLua.eval("return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}", keys, args);
        System.out.println(eval);
    }

    @Test
    public  void redisLua1(){
        jedisUtils.redisStr.set("product_stock_10016","15");
        String script = " local count = redis.call('get', KEYS[1]) " +
                // 将值转换为数据类型
                " local a = tonumber(count) " +
                // 将值转换为数据类型
                " local b = tonumber(ARGV[1]) " +
                //  当前值如果大于要修改的值，则修改，否则不修改
                " if a >= b then " +
                "   redis.call('set', KEYS[1], a-b) " +
                //模拟语法报错回滚操作
                // "   bb == 0 " +
                "   return 1 " +
                " end " +
                " return 0 ";

        Object product_stock_10016 = jedisUtils.redisLua.eval(script, Collections.singletonList("product_stock_10016"), Collections.singletonList("10"));

    }


    @Test

    public void testRedis6() {
        testRedis5();
    }
    @JedisConfig(db = 1)
    public void testRedis5() {
        String testdb = jedisUtils.redisStr.set("testdb", "111");
        System.out.println(testdb);
    }
}
