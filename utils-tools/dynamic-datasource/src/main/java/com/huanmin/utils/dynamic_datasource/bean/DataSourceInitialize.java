package com.huanmin.utils.dynamic_datasource.bean;


import com.huanmin.utils.common.base.UniversalException;

import com.huanmin.utils.dynamic_datasource.base.DynamicDataSourceService;
import com.huanmin.utils.dynamic_datasource.dal.mapper.DatasourceMapper;
import com.huanmin.utils.dynamic_datasource.entity.DataSourceEneity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

//从数据库中查询出全部的数据源,添加到数据源容器中

/**
 * 表结构初始化文件: resources/sql/init.sql
 */

//依赖于DynamicDataSourceConfig中的abstractRoutingDataSource
@DependsOn("abstractRoutingDataSource")
@Component
@Slf4j
public class DataSourceInitialize implements ApplicationRunner  {

    @Autowired
    private DatasourceMapper datasourceMapper;

    @Value("${spring.datasource.database-load-activate}")
    private Boolean databaseLoadActivate;

    //项目启动后执行初始化数据源
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (databaseLoadActivate){
            log.info("-----------------DataSourceInitialize------------------");
            try {
                List<DataSourceEneity> dataSources = datasourceMapper.getDataSources();
                for (DataSourceEneity dataSource : dataSources) {
                    DynamicDataSourceService.addDataSource(dataSource.getKey(),dataSource.getDataSource());
                }
            } catch (Exception e) {
                UniversalException.logError(e);
            }
        }
    }
}
