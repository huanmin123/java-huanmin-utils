package org.huanmin.timerwheel.timerannotation;

public enum Distributed {
    NONE,
    REDIS,  //redis分布式锁 ,需要引入redis依赖
    ZOOKEEPER,
    KAFKA,
    RABBITMQ,
    MYSQL
}
