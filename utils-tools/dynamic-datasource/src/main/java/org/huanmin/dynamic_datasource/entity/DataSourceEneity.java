package org.huanmin.dynamic_datasource.entity;

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
    private String type;
    private int state;


    public  DataSource getDataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.driverClassName(driverClassName);
        builder.username(username);
        builder.password(password);
        builder.url(url);
        return  builder.build();
    }

}
