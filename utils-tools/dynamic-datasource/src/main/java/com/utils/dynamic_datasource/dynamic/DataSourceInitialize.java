package com.utils.dynamic_datasource.dynamic;


import com.utils.common.base.UniversalException;
import com.utils.dynamic_datasource.dal.mapper.DatasourceDao;
import com.utils.dynamic_datasource.dynamic.DynamicDataSourceService;
import com.utils.dynamic_datasource.entity.DataSourceEneity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//从数据库中查询出全部的数据源,添加到数据源容器中

/**
 * 表结构如下:

 CREATE TABLE `t_datasource` (
   `id` int(11) NOT NULL,
   `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '绑定的key,用于数据源的切换',
   `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库连接地址',
   `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库用户名',
   `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库密码',
   `driverClassName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库驱动',
   `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库类型:  mysql ,oracle,..',
   `state` int(2) NOT NULL COMMENT '是否可用: 1可用 ,2不可用',
   PRIMARY KEY (`id`),
   UNIQUE KEY `key` (`key`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

 * 上表要放入到默认数据源中的数据库里才行
 */

@Component
public class DataSourceInitialize implements ApplicationRunner  {

    @Autowired
    private DatasourceDao datasourceDao;
    @Value("${spring.datasource.database-load-activate}")
    private Boolean databaseLoadActivate;

    //项目启动后执行初始化数据源
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (databaseLoadActivate){
            try {
                List<DataSourceEneity> dataSources = datasourceDao.getDataSources();
                for (DataSourceEneity dataSource : dataSources) {
                    DynamicDataSourceService.addDataSource(dataSource.getKey(),dataSource.getDataSource());
                }
            } catch (Exception e) {
                UniversalException.logError(e);
            }
        }
    }
}
