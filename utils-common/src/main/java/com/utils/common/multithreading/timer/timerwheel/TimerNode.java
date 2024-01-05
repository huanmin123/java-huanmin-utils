package com.utils.common.multithreading.timer.timerwheel;

import com.utils.common.multithreading.timer.timerwheel.timerannotation.TimerResolver;

import java.util.function.Function;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/30 17:04
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class TimerNode {
    TimerNode next; //指向下一个节点
    long expires; //到期时间  秒时间戳
    TimerResolverCore timerResolver; //任务计划解析后对象
    String  strResolver; //保留任务计划字符串,用于后期重新解析
    Function func; //回调函数
    Object args; //回调函数参数
    String timer_id; //定时器名称
    int timer_status; //定时器状态(0:停止,1:启动,2:待删除,3确认删除,4重启任务)
    int timer_dynamic; //0:等待执行,1:执行中 (用于展示任务的状况)
    Object returnValue; //任务的返回值
}
