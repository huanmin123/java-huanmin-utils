package com.huanmin.test.utils.utils_common.spring;


import com.huanmin.test.TestApplication;
import com.huanmin.test.entity.UserEsEneity;
import com.huanmin.utils.common.spring.AutoBeanInject;
import com.huanmin.utils.common.spring.SpringContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class ApplicatioBootTest {

    @Test
    public void test2() throws InterruptedException {
        AutoBeanInject.addBeanField("testBean", UserEsEneity.class,null,null);
//
        //获取bean
        UserEsEneity testBean = (UserEsEneity) SpringContextHolder.getBean("testBean");
        String s = testBean.toAction("动态注册生成调用");
        System.out.println(s);
    }


    @Test
    public void test3() throws InterruptedException {
        Map<String, Object> basics=new HashMap<>();
        basics.put("age",123);
        basics.put("name","ddd");
        Map<String, String> quote=new HashMap<>();
        quote.put("xssAop","xssAop"); //从容器中找到xssAop的bean然后注入xssAop这个属性中
        AutoBeanInject.addBeanField("testBean",UserEsEneity.class,basics,null);
        //获取bean
        UserEsEneity testBean = (UserEsEneity) SpringContextHolder.getBean("testBean");
        String s = testBean.toAction("动态注册生成调用");
        System.out.println(s);
        System.out.println(String.valueOf(testBean));
    }


}
