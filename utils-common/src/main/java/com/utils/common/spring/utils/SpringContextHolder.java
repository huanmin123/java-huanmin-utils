package com.utils.common.spring.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *Springboot动态获取ioc容器bean对象工具类

 **/
@Component("springContextHolder")
public class SpringContextHolder implements ApplicationContextAware {

    /**
     *       * 上下文对象实例
     *
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 获取Spring上下文
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取Bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }


    /**
     *
     * @param key  配置文件的key   比如spring.application.name
     * @return  返回value
     */
    public static String getConfig(String key) {
       return Optional.ofNullable(getApplicationContext().getEnvironment().getProperty(key)).orElse("");
    }
    
    //获取bean的名字
    public static String[] getBeanNamesForType(Class<?> type) {
        return getApplicationContext().getBeanNamesForType(type);
    }


}