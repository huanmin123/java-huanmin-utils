package com.utils.dynamic_datasource.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.utils.common.spring.SpringContextHolder;
import com.utils.dynamic_datasource.aop.DynamicDataSourceAspect;
import com.utils.dynamic_datasource.bean.DataSourceInitialize;
import com.utils.dynamic_datasource.bean.DynamicDataSourceConfig;
import com.utils.dynamic_datasource.bean.YmlDataSourceProvider;
import org.springframework.context.annotation.ComponentScan;
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
        MybatisPlusAutoConfiguration.class,
        YmlDataSourceProvider.class,
        DataSourceInitialize.class,
        DynamicDataSourceAspect.class,
}
)
public class DynamicDatasourceMainConfig {



}
