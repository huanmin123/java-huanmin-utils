package com.utils.dynamic_datasource.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.utils.common.spring.utils.SpringContextHolder;
import com.utils.dynamic_datasource.bean.DataSourceInitialize;
import com.utils.dynamic_datasource.bean.DynamicDataSourceConfig;
import com.utils.dynamic_datasource.bean.YmlDataSourceProvider;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

import static com.baomidou.mybatisplus.extension.toolkit.SqlHelper.sqlSessionFactory;

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

}
)
public class DynamicDatasourceMainConfig {

    @PostConstruct
    public void init() {
        System.out.println("DynamicDatasourceMainConfig");
    }


}
