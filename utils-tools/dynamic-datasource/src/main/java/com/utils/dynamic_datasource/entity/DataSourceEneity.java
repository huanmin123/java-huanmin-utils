package com.utils.dynamic_datasource.entity;

import lombok.Data;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Data
public class DataSourceEneity {

    private int id;
    private String key;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String pool;
    private String type;
    private int state;


    public  DataSource getDataSource() {
        DataSourceBuilder builder =  DataSourceBuilder.create();
        try {
            builder.driverClassName(driverClassName);
            builder.username(username);
            builder.password(password);
            builder.url(url);
            //将pool转换为class
            Class<?> aClass = Class.forName(pool);
            builder.type(aClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return  builder.build();
    }

}
