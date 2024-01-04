package com.utils.dynamic_datasource.bean;


import com.utils.common.base.UniversalException;
import com.utils.common.db.SqlSessionTemplateUtil;
import com.utils.dynamic_datasource.base.DynamicDataSourceService;
import com.utils.dynamic_datasource.dal.mapper.DatasourceDao;
import com.utils.dynamic_datasource.entity.DataSourceEneity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private DatasourceDao datasourceDao;

    @Value("${spring.datasource.database-load-activate}")
    private Boolean databaseLoadActivate;

    //项目启动后执行初始化数据源
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (databaseLoadActivate){
            log.info("-----------------DataSourceInitialize------------------");
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
