package org.huanmin.test.utils.utils_common.db;

import org.huanmin.utils.common.base.UniversalException;
import org.huanmin.utils.jdbc_extend.jdbc.JdbcTemplateUtil;
import org.huanmin.utils.jdbc_extend.jdbc.ConnectionPond;
import org.huanmin.utils.jdbc_extend.jdbc.JdbcUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class JdbcTemplateUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateUtilsTest.class);
    @Test
    public  void select1() {
        //查询
        String sql = "select * from t_user  where username =?";
        //上面?号 对应下面的参数
        Map<String,Object> map= null;
        try {

            map = JdbcTemplateUtil.jdbcTemplate.queryForMap(sql,"hu");
        } catch (DataAccessException e) {
             UniversalException.logError(e);
        }

        System.out.println(String.valueOf(map));
    }
    @Test
    public  void select2() throws Exception {
        ConnectionPond connection = JdbcUtil.getConnectionPond();
        connection.setAutoCommitStart();
        String sql="SELECT lock_key,lock_until FROM t_lock    WHERE   lock_key='show3' FOR update";
        PreparedStatement  preparedStatement=connection.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(String.valueOf(resultSet));

        System.out.println("----");

        Thread.sleep(1000000);


    }

    @Test
    public  void select23() throws Exception {
        ConnectionPond connection = JdbcUtil.getConnectionPond();
        connection.setAutoCommitStart();
        String sql="SELECT lock_key,lock_until FROM t_lock    WHERE   lock_key='show3' FOR update";
        PreparedStatement  preparedStatement=connection.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(String.valueOf(resultSet.next()));

        Thread.sleep(10000);


    }

}
