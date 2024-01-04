package com.utils.dynamic_datasource.dynamic;

import com.utils.dynamic_datasource.dynamic.DynamicDataSourceService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文件读取yml里的数据源配置
 */
@Component
@Data
@DependsOn("springContextHolder") //当前bean初始化之前,优先加载指定的bean
@ConfigurationProperties(prefix = "spring.datasource")
@Slf4j
public class YmlDataSourceProvider  {

    private List<Map<String, DataSourceProperties>> multiDb;

    private DataSource buildDataSource(DataSourceProperties prop) {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.driverClassName(prop.getDriverClassName());
        builder.username(prop.getUsername());
        builder.password(prop.getPassword());
        builder.url(prop.getUrl());
        return builder.build();
    }
    public void initDataSource() {
        log.info("-----------------YmlDataSourceProvider------------------");
        multiDb.forEach(map -> {
            Set<String> keys = map.keySet();
            keys.forEach(key -> {
                DataSourceProperties properties = map.get(key);
                DataSource dataSource = buildDataSource(properties);
                DynamicDataSourceService.addDataSource(key, dataSource);
            });
        });
    }

    //在构造函数之后执行
    @PostConstruct
    public void init() {
        initDataSource();
    }

}
