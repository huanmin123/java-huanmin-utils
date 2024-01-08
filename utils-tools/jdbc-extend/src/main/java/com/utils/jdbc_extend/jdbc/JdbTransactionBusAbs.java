package com.utils.jdbc_extend.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class JdbTransactionBusAbs implements JdbTransaction {
    protected  List<JdbcObj> jdbcObjs;

    public JdbTransactionBusAbs(List<JdbcObj> jdbcObjs)  {
        this.jdbcObjs=jdbcObjs;

    }

    //执行sql
    protected  Object execute(JdbcObj jdbcObj ) throws Exception{
        switch (jdbcObj.getType()) {
            case DELETE:
                //int execute = (int)execute(jdbcObj2);
              return   jdbcObj.getPreparedStatement().executeUpdate();
            case INSERT:
                return  jdbcObj.getPreparedStatement().executeUpdate();
            case UPDATE:
                return  jdbcObj.getPreparedStatement().executeUpdate();
            case SELECT:
                //ResultSet execute = (ResultSet)execute(jdbcObj2);
                return jdbcObj.getPreparedStatement().executeQuery();
            default:
                throw new Exception("没有找到执行sql的类型");
        }
    }
    //自动组合sql 和参数
    protected  JdbcObj  sqlInit(Connection connection,JdbcObj  jdbcObj) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcObj.getSql());
        if (jdbcObj.getArgs()!=null) {
            for (int j = 1; j <= jdbcObj.getArgs().length; j++) {
                preparedStatement.setObject(j, jdbcObj.getArgs()[j - 1]);
            }
        }
        jdbcObj.setPreparedStatement(preparedStatement);
        return jdbcObj;
    }
}
