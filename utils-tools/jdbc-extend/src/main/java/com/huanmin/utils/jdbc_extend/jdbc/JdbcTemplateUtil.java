package com.huanmin.utils.jdbc_extend.jdbc;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.huanmin.utils.common.base.UniversalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;


public class JdbcTemplateUtil {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateUtil.class);
    public   static Connection connection=null;
    public   static JdbcTemplate jdbcTemplate=null;
    //调用properties 文件的 数据库连接
    static {
        //1.创建一个Propertis 集合
        Properties pro=new Properties();
        try {
            pro.load(JdbcTemplateUtil.class.getClassLoader().getResourceAsStream("druid.properties"));
        } catch (IOException e) {
             UniversalException.logError(e);
        }
        //创建一个DataSource
        try {
            DataSource dataSource= DruidDataSourceFactory.createDataSource(pro);
            jdbcTemplate=new JdbcTemplate((DataSource) dataSource);
            connection= Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();

        } catch (Exception e) {
             UniversalException.logError(e);
        }

        //不用 管连接的创建  因为  JdbcTemplate  他内部 会自动给你 创建 和关闭的
        //除非 你手动 去调用 Connection

    }


    public static void  setAutoCommitStart() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
             UniversalException.logError(e);
        }
    }


    public static void  commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
             UniversalException.logError(e);
        }
    }

    public static void  rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
             UniversalException.logError(e);
        }
    }

    public static void  setAutoCommitEnd() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
             UniversalException.logError(e);
        }
    }

    public static void  execute(String sql ,Object... args) {
        jdbcTemplate.update(sql,args);
    }
    public static void  query(String sql, RowCallbackHandler rch , Object... args) {
        jdbcTemplate.query(sql,rch,args);
    }
    public static List<Map<String, Object>>  queryList(String sql, Object... args) {
       return jdbcTemplate.queryForList(sql, args);
    }
    public static <T> List<T>  queryList(String sql,Class<T> cl, Object... args) {
        return jdbcTemplate.queryForList(sql,cl,args);
    }
    public static Map<String, Object>  queryMap(String sql, Object... args) {
        return jdbcTemplate.queryForMap(sql,args);
    }
    public static int[]  batchUpdate(String... sql) {
        return jdbcTemplate.batchUpdate(sql);
    }


}

