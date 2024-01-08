package com.utils.jdbc_extend.jdbc;

import com.utils.common.base.UniversalException;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public  class ConnectionPond {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPond.class);
    private  String key;
    private Connection connection;
    //连接是否使用中
    private AtomicBoolean use=new AtomicBoolean(false);
    //连接空闲时间(默认30分钟)
    private  LocalDateTime localDateTime=null;

    //使用中
    public  void  setUseTrue() {
        this.use.set(true);
    }
    //使用完毕
    public  void  setUseFalse() {
        this.use.set(false);
    }



    public void setLocalDate() { //刷新空闲时间
        this.localDateTime = LocalDateTime.now();
    }
    public  void expire(Map<String,ConnectionPond> map) {
        //空闲时间<当前时间
        boolean before = localDateTime.isBefore(LocalDateTime.now());
        //到期,并且当前连接没有被使用,并且当前连接池连接数量>30
        if (before&&!use.get()&&map.size()>30) {
            System.out.println("当前连接数量"+map.size()+"------连接清理: "+this);
            //关闭和数据库的连接
            try {
                connection.close();
            } catch (SQLException e) {
                 UniversalException.logError(e);
            }
            //删除在线程池中的对象
            map.remove(this.getKey());

        }
    }

    public ConnectionPond() {
        connectionPond(null);
    }

    public ConnectionPond(String key) {
        connectionPond(key);
    }
    public void connectionPond(String key) {
        try {
            Properties properties = JdbcUtil.getProperties();
            this.connection= DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("username"),properties.getProperty("password"));
        } catch (SQLException e) {
             UniversalException.logError(e);
        }
        //创建空闲时间为30分钟(当前时间+30分钟)  如果空闲时间超过30分钟那么就删除连接
        this.localDateTime= LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
        if (StringUtils.isBlank(key)) {
            this.key= UUID.randomUUID().toString();
        } else {
            this.key=key;
        }

    }


    public  void  setAutoCommitStart() {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
             UniversalException.logError(e);
        }
    }

    public  void  commit() {
        try {
            getConnection().commit();
        } catch (SQLException e) {
             UniversalException.logError(e);
        }
    }

    public  void  rollback() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
             UniversalException.logError(e);
        }
    }

    public  void  setAutoCommitEnd() {
        try {
            getConnection().setAutoCommit(true);
        } catch (SQLException e) {
             UniversalException.logError(e);
        }
    }


}
