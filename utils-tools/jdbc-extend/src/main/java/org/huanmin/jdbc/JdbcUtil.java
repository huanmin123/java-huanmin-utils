package org.huanmin.jdbc;


import com.utils.common.base.UniversalException;
import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.utils.SleepTools;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcUtil {
    private static Properties properties = new Properties();
    private static Map<String,ConnectionPond> map = new ConcurrentHashMap<>();

    static {
        InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("database.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
             UniversalException.logError(e);
        }
        try {
            Class.forName(properties.getProperty("driver"));
        } catch (ClassNotFoundException e) {
             UniversalException.logError(e);
        }


        initialize();
        timeClock();
    }

    public static Properties getProperties() {
        return properties;
    }

    //初始化30个连接
    private static void initialize() {
        for (int i = 0; i < 30; i++) {
            ConnectionPond connectionPond = new ConnectionPond();
            map.put(connectionPond.getKey(),connectionPond);
        }
    }

    private static void timeClock() {
        ExecutorUtil.newThreadDaemon(() -> {
            while (true) {
                for (Map.Entry<String, ConnectionPond> stringConnectionPondEntry : map.entrySet()) {
                    ConnectionPond connectionPond = stringConnectionPondEntry.getValue();
                    connectionPond.expire(map);
                }
                SleepTools.second(300); //5分钟执行一次清除连接策略
            }
        });
    }


    public static ConnectionPond getConnectionPond() {
        for (Map.Entry<String, ConnectionPond> stringConnectionPondEntry : map.entrySet()) {
            ConnectionPond connectionPond = stringConnectionPondEntry.getValue();
            if (!connectionPond.getUse().get()) {
                synchronized (JdbcUtil.class) { //获取互斥
                    if (!connectionPond.getUse().get()) {
                        connectionPond.setUseTrue();//设置使用中
                        connectionPond.setLocalDate();//刷新空闲时间
                        return connectionPond;
                    }
                }
            }
        }
        //没有多余的连接了.那么创建新的连接
        ConnectionPond connectionPond = new ConnectionPond();
        connectionPond.setUseTrue();//设置使用中
        map.put(connectionPond.getKey(),connectionPond);
        return connectionPond;
    }
    //事物使用, 相同的事物一个key
    public static ConnectionPond getConnectionPond(String key) {
        ConnectionPond connectionPond1 = map.get(key);
        if (connectionPond1 == null) {
            synchronized (JdbcUtil.class) { //获取互斥
                connectionPond1 = map.get(key);
                if (connectionPond1 == null) {
                    //创建新的连接
                    ConnectionPond connectionPond = new ConnectionPond(key);
                    connectionPond.setUseTrue();//设置使用中
                    map.put(connectionPond.getKey(),connectionPond);
                    connectionPond1=connectionPond;
                }
            }
        } else {
            connectionPond1.setLocalDate();//刷新空闲时间
        }

        return connectionPond1;
    }

    public static Map<String, Object> query(String sql, Object... args) {
        ConnectionPond connectionPond = getConnectionPond();
        return query(connectionPond,sql,args);
    }

    public static Map<String, Object> query(ConnectionPond connectionPond,String sql, Object... args) {
        Map<String, Object> map = new HashMap<>(30);

        try {
            PreparedStatement preparedStatement = connectionPond.getConnection().prepareStatement(sql);
            for (int i = 1; i <= args.length; i++) {
                preparedStatement.setObject(i, args[i - 1]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            if (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    map.put(rsmd.getColumnName(i), resultSet.getObject(i));
                }
            }
            resultSet.close();
        } catch (SQLException e) {
             UniversalException.logError(e);
        } finally {
            //使用完毕
            connectionPond.setUseFalse();
        }
        return map;
    }

    public static List<Map<String, Object>> querys(String sql, Object... args) {
        ConnectionPond connectionPond = getConnectionPond();
        return  querys(connectionPond,sql,args);
    }

    public static List<Map<String, Object>> querys(ConnectionPond connectionPond,String sql, Object... args) {
        List<Map<String, Object>> maps = new ArrayList<>(1000);

        try {
            PreparedStatement preparedStatement = connectionPond.getConnection().prepareStatement(sql);
            for (int i = 1; i <= args.length; i++) {
                preparedStatement.setObject(i, args[i - 1]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            maps = dataGroup(resultSet);
        } catch (SQLException e) {
             UniversalException.logError(e);
        } finally {
            //使用完毕
            connectionPond.setUseFalse();
        }
        return maps;
    }


    public  static List<Map<String, Object>>  dataGroup(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> maps = new ArrayList<>(1000);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>(30);
            for (int i = 1; i <= columnsNumber; i++) {
                map.put(rsmd.getColumnName(i), resultSet.getObject(i));
            }
            maps.add(map);
        }
        resultSet.close();
        return maps;
    }


    //增删改
    public static int execute(String sql, Object... args) {
        ConnectionPond connectionPond = getConnectionPond();
        return execute(connectionPond,sql,args);
    }

    public static int execute(ConnectionPond connectionPond,String sql, Object... args) {
        int i1 = 0;
        try {
            PreparedStatement preparedStatement = connectionPond.getConnection().prepareStatement(sql);
            for (int i = 1; i <= args.length; i++) {
                preparedStatement.setObject(i, args[i - 1]);
            }
            i1 = preparedStatement.executeUpdate();
        } catch (SQLException e) {
             UniversalException.logError(e);
        } finally {
            //使用完毕
            connectionPond.setUseFalse();
        }
        return i1;
    }


    //继承JdbcBusTask接口实现run方法
    public static  boolean transaction(JdbTransaction jdbTransaction) {
      return  transaction(jdbTransaction,null);
    }

    //继承JdbcBusTask接口实现run方法
    public static  boolean transaction(JdbTransaction jdbTransaction,String key) {
        ConnectionPond connectionPond = null;
        if (StringUtils.isBlank(key)) {
            connectionPond = JdbcUtil.getConnectionPond();
        } else {
            connectionPond = JdbcUtil.getConnectionPond(key);
        }
        try {
            //开启事物
            connectionPond.setAutoCommitStart();
            jdbTransaction.run(connectionPond.getConnection());
            //提交事物
            connectionPond.commit();
            return true;
        } catch (Exception e) {
             UniversalException.logError(e);
            //回滚事物
            connectionPond.rollback();
        } finally {
            //关闭事物
            connectionPond.setAutoCommitEnd();
            //使用完毕
            connectionPond.setUseFalse();
        }

        return  false;
    }
}
