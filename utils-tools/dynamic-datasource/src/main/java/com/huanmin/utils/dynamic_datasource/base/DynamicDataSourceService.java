package com.huanmin.utils.dynamic_datasource.base;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.huanmin.utils.common.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 手动添加数据源配置
 */
public class DynamicDataSourceService  {
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceService.class);

    private static final Map<Object, Object> dataSources = new HashMap<>();
    private static final TransmittableThreadLocal<String> dbKeys = new TransmittableThreadLocal<>();

    /**
     * 动态添加一个数据源
     *
     * @param name       数据源的key
     * @param dataSource 数据源对象
     */
    public static void addDataSource(String name, DataSource dataSource) {
        DynamicDataSource dynamicDataSource = SpringContextHolder.getApplicationContext().getBean(DynamicDataSource.class);
        dataSources.put(name, dataSource);
        dynamicDataSource.setTargetDataSources(dataSources);
        dynamicDataSource.afterPropertiesSet();
        log.info("添加了数据源:{}",name);
    }

    /**
     * @param name   数据源的key
     * @param driverClassName  驱动
     * @param url     数据库连接地址
     * @param username   数据库账户
     * @param password   数据库密码
     */
    public static void addDataSource(String name, String driverClassName,String url,String username,String password) {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.driverClassName(driverClassName);
        builder.username(username);
        builder.password(password);
        builder.url(url);
        addDataSource(name,builder.build());
        log.info("添加了数据源:{}",name);
    }
    /**
     * 切换数据源
     */
    public static void switchDb(String dbKey) {
        dbKeys.set(dbKey);
    }

    /**
     * 重置数据源(切换为默认的数据源)
     */
    public static void resetDb() {
        dbKeys.remove();
    }

    /**
     * 获取当前数据源的key
     */
    public static String currentDb() {
        return dbKeys.get();
    }




}
