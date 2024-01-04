package com.utils.dynamic_datasource.config;

import com.utils.common.spring.utils.SpringContextHolder;
import com.utils.dynamic_datasource.bean.DataSourceInitialize;
import com.utils.dynamic_datasource.bean.DynamicDataSourceConfig;
import com.utils.dynamic_datasource.bean.YmlDataSourceProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author huanmin
 * @date 2024/1/4
 */
@Configuration
@Import({
        SpringContextHolder.class,
        DynamicDataSourceConfig.class,
        YmlDataSourceProvider.class,
        DataSourceInitialize.class,
}
)
public class DynamicDatasourceMainConfig {
}
