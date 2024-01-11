package com.huanmin.utils.common.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

//注意  如果注入容器里有beanName相同的了,那么会注入失败的
// 解决办法就是删除原来的bean然后添加现在的bean(没有必要不建议使用)

//        判断beanName 这个名称是否已经在工厂中存在了
//        defaultListableBeanFactory.isBeanNameInUse()
//        删除容器里的bean.
//        defaultListableBeanFactory.removeBeanDefinition("testService");

public class AutoBeanInject {

    //属性方式自动将class注入到spring容器中   ,属性必须实现set方法,而且必须有无惨构造 ,只要少一个就会报错
    /**
     *  (推荐 ,这种方式比较灵活)
     * @param beanName 注入容器bean的名称
     * @param clazz  需要注入的类
     * @param basics  基础类型
     * @param quote   需要从容器里注入到变量上的属性,  如果类中使用自动注入 @Autowired 等注解 ,那么可以不需要我们手动的去指定注入的Bean
     */

    public static void addBeanField(String beanName, Class clazz, Map<String, Object> basics, Map<String, String> quote) {
        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();

        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();

        // 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);


        if (basics!=null) {
            //给基本数据类型赋值
            for (Map.Entry<String, Object> stringObjectEntry : basics.entrySet()) {
                beanDefinitionBuilder.addPropertyValue(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        }
        if (quote!=null) {
            //将容器中的指定bean注入到当前类的指定属性对象上
            for (Map.Entry<String, String> stringObjectEntry : quote.entrySet()) {
                //如果指定了属性值设置为Autowired   那么就自动装配(从容器中选取一个这个属性对象的子类或者实现类来装配)
                if ("Autowired".equals(stringObjectEntry.getValue())) {
                    beanDefinitionBuilder.addAutowiredProperty(stringObjectEntry.getKey());
                }
                beanDefinitionBuilder.addPropertyReference(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        }
        // 注册bean   将 beanName 注册到容器里了
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());

    }

    //Structure方式自动将class注入到spring容器中  ,必须存在构造函数,并且安装构造参数的顺序依次赋值,从左到右

    /**
     *
     * @param beanName 注入容器bean的名称
     * @param clazz  需要注入的类
     * @param map   给对应构造函数赋值 ,要和构造函数里的参数顺序一致  基础类型的key=common   需要容器注入的类型 key=container
     *
     */

    public static void addBeanStructure(String beanName, Class clazz, Map<String, Object> map) {
        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();

        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();

        // 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);

        if (map!=null) {
            //给基本数据类型赋值
            for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
                //普通类型注入
                if ("common".equals(stringObjectEntry.getKey())) {
                    beanDefinitionBuilder.addConstructorArgValue(stringObjectEntry.getValue());
                } else if ("container".equals(stringObjectEntry.getKey())){
                    //容器bean注入到指定的对象属性上   注意是容器里的bean的名称
                    beanDefinitionBuilder.addConstructorArgReference(String.valueOf(stringObjectEntry.getValue()));
                }
            }
        }
        // 注册bean   将 beanName 注册到容器里了
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());

    }
    
    //删除容器里的bean
    public static void removeBean(String beanName) {
        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();

        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();

        //删除容器里的bean.
        defaultListableBeanFactory.removeBeanDefinition(beanName);
    }
    //替换容器里的bean
    public static void replaceBean(String beanName, Class clazz, Map<String, Object> basics, Map<String, String> quote) {
        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();
    
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
    
        //删除容器里的bean.
        defaultListableBeanFactory.removeBeanDefinition(beanName);
    
        // 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
    
        if (basics != null) {
            //给基本数据类型赋值
            for (Map.Entry<String, Object> stringObjectEntry : basics.entrySet()) {
                beanDefinitionBuilder.addPropertyValue(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        }
        if (quote != null) {
            //将容器中的指定bean注入到当前类的指定属性对象上
            for (Map.Entry<String, String> stringObjectEntry : quote.entrySet()) {
                //如果指定了属性值设置为Autowired   那么就自动装配(从容器中选取一个这个属性对象的子类或者实现类来装配)
                if ("Autowired".equals(stringObjectEntry.getValue())) {
                    beanDefinitionBuilder.addAutowiredProperty(stringObjectEntry.getKey());
                }
                beanDefinitionBuilder.addPropertyReference(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        }
        // 注册bean   将 beanName 注册到容器里了
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
    }
    
    // 重新注册bean
    public static void reRegisterBean(Class<?> implBeanClz, String beanName) {
        replaceBean(beanName, implBeanClz, null, null);
    }

}
