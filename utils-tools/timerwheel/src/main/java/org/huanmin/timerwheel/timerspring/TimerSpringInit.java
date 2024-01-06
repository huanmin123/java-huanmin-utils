package org.huanmin.timerwheel.timerspring;

import com.utils.common.base.UniversalException;
import org.apache.commons.lang.StringUtils;
import org.huanmin.timerwheel.Timer;
import org.huanmin.timerwheel.timerannotation.Distributed;
import org.huanmin.timerwheel.timerannotation.TimerResolver;
import org.huanmin.timerwheel.timerannotation.TimerWheel;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

//用于在springboot框架中启动进行初始化定时器
//@Component
public class TimerSpringInit implements ApplicationListener<ContextRefreshedEvent> {

   private Timer pTimer = new Timer();
    //获取spring里所有被CustomTimer注解的Bean的Class
    public List<Class> getBean(AnnotationConfigServletWebServerApplicationContext annotation,Class cl) {
        List<Class> classes=new ArrayList<>();
        String[] beanNamesForAnnotation = annotation.getBeanNamesForAnnotation(cl);
        for (String beanName : beanNamesForAnnotation) {
            Class<?> aClass = annotation.getBean(beanName).getClass();
            classes.add(aClass);
        }
        return  classes;
    }
    //构建定时器
    public  void   structureTimer(List<Class> classes)  {
        try {
            for (Class aClass : classes) {
                Object o = aClass.newInstance();
                //1.先获取每个方法上的指定注解
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    try {
                        TimerResolver timerResolver = method.getAnnotation(TimerResolver.class);
                        if (timerResolver==null) {
                            continue;
                        }
                        String key=aClass.getName()+"."+method.getName();
                        String resolver = timerResolver.resolver();
                        if (StringUtils.isEmpty(resolver)) {
                            continue;
                        }
                        pTimer.add_timer_sask( resolver,key,(data)->{
                            Object invoke=null;
                            try {
                              invoke = method.invoke(o);//执行方法
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                            return invoke;
                        }, null);

                    } catch (Exception e) {
                         UniversalException.logError(e);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
             UniversalException.logError(e);
        }

    }

    public void add_timer_sask_lock( String timer_resolver, String timer_name, Function func, Object args, Distributed distributed) throws Exception {
        switch (distributed){
            case REDIS: //目前只支持redis分布式锁
                pTimer.add_timer_sask( timer_resolver, timer_name, func, args);
                break;
            case ZOOKEEPER:
                break;
            case KAFKA:
                break;
            case RABBITMQ:
                break;
            case MYSQL:
                break;
            default: //如果不是分布式任务,则直接添加到任务列表
                pTimer.add_timer_sask( timer_resolver, timer_name, func, args);
                break;
        }
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AnnotationConfigServletWebServerApplicationContext annotation = (AnnotationConfigServletWebServerApplicationContext) event.getSource();
        ApplicationContext applicationContext = event.getApplicationContext();
        List<Class> bean = getBean(annotation, TimerWheel.class);//获取所有被Timer修饰的类
        structureTimer(bean);
        //将定时器放入spring容器中,通过注入的方式获取
        annotation.getBeanFactory().registerSingleton("timerWheel",pTimer);
    }



}
