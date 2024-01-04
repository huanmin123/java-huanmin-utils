package com.utils.dynamic_datasource.dynamic;

import com.utils.dynamic_datasource.dynamic.DynamicDataSource;
import lombok.Data;
import org.apache.ibatis.logging.Log;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "mybatis")
@Data
public class DynamicDataSourceConfig {

    private String mapperLocations;
    private String typeAliasesPackage;
    @Data
    public class MybatisConfiguration{
        private String logImpl;
        private boolean mapUnderscoreToCamelCase;
    }
    private  MybatisConfiguration configuration=new MybatisConfiguration();


    /**
     * 动态数据源
     */
    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }

    /**
     * 会话工厂
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException, ClassNotFoundException {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(this.configuration.isMapUnderscoreToCamelCase()); //开启驼峰命名
        configuration.setLogImpl((Class<? extends Log>) Class.forName(this.configuration.getLogImpl())); //控制台打印sql日志
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        sqlSessionFactoryBean.setConfiguration(configuration);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperLocations));
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        return sqlSessionFactoryBean;
    }

    /**
     * 事务管理器
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}
