package org.huanmin.utils.common.base;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * @author huanmin
 * @date 2023/11/21
 */
@Slf4j
public class UniversalException extends RuntimeException {
    //format
    public UniversalException(String message, Object... args) {
        //将{}替换为%s
        super(String.format(message.replaceAll("\\{\\s*\\}", "%s"), args));
        log.error(message, args);
    }
    //有些错误是通过e.getMessage()获取不到的,所以需要传入e拿到堆栈信息
    //但是直接打印堆栈信息是加锁的导致有性能问题,所以我们需要通过让printStackTrace接收一个输出流来避免加锁并且按照日志的方式打印堆栈信息
    public UniversalException(Throwable e, String message, Object... args) {
        //将{}替换为%s
        super(String.format(message.replaceAll("\\{\\s*\\}", "%s"), args),e);
        //生产唯一的uuid,便于定位
        String uuid = UUID.randomUUID().toString();
        log.error(uuid+"###"+message, args);
        //打印堆栈
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        log.error(uuid+"###"+expMessage);
    }
    public UniversalException(Throwable e) {
        super(e);
        //打印堆栈
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        log.error(expMessage);
    }
    public  static void  logError(Throwable e,String message, Object... args){
        //生产唯一的uuid,便于定位
        String uuid = UUID.randomUUID().toString();
        log.error(uuid+"###"+message, args);
        //打印堆栈
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        log.error(uuid+"###"+expMessage);
    }
    public  static void  logError(Throwable e){
        //打印堆栈
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        log.error(expMessage);
    }
}
