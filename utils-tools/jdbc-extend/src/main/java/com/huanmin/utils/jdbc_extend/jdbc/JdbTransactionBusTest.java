package com.huanmin.utils.jdbc_extend.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class JdbTransactionBusTest extends JdbTransactionBusAbs {
    private static final Logger logger = LoggerFactory.getLogger(JdbTransactionBusTest.class);
    public JdbTransactionBusTest(List<JdbcObj> jdbcObjs) throws Exception {
        super(jdbcObjs);
    }
    @Override
    public void run(Connection connection) throws Exception {
        JdbcObj jdbcObj1= jdbcObjs.get(0);
        JdbcObj jdbcObj2 = sqlInit(connection,jdbcObj1 );
        //进行执行查询操作
        ResultSet execute = (ResultSet)execute(jdbcObj2);
        if (execute.next()) {
            int id = execute.getInt("id");
            System.out.println(String.valueOf(id));

            JdbcObj jdbcObj3 = jdbcObjs.get(1);
            jdbcObj3.setArgs(id);
            JdbcObj jdbcObj4 = sqlInit(connection, jdbcObj3);
            //进行执行修改操作
            int execute1 = (int)execute(jdbcObj4);
            System.out.println(String.valueOf(execute1));
        }



    }
}
