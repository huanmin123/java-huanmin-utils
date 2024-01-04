package com.utils.dynamic_datasource.config;

import com.utils.dynamic_datasource.dal.mapper.DatasourceDao;
import com.utils.dynamic_datasource.dynamic.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author huanmin
 * @date 2024/1/4
 */
@Configuration
@Import({DataSourceInitialize.class,
        DynamicDataSource.class,
        DynamicDataSourceConfig.class,
        DynamicDataSourceService.class,
        YmlDataSourceProvider.class,
        DatasourceDao.class
}
)
public class DynamicDatasourceMainConfig {
}
