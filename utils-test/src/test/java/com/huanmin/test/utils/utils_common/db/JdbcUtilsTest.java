package com.huanmin.test.utils.utils_common.db;

import com.huanmin.utils.jdbc_extend.jdbc.JdbTransactionBusTest;
import com.huanmin.utils.jdbc_extend.jdbc.JdbcEnum;
import com.huanmin.utils.jdbc_extend.jdbc.JdbcObj;
import com.huanmin.utils.jdbc_extend.jdbc.JdbcUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class JdbcUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(JdbcUtilsTest.class);
    @Test
    public  void select() {
        List<Map<String, Object>> querys = JdbcUtil.querys("select * from t4");

        System.out.println(String.valueOf(querys));
    }

    @Test
    public  void execute() {
        int xxxxxx = JdbcUtil.execute("update t_user set username=?  where id=11", "xxxxxx");
        System.out.println(String.valueOf(xxxxxx));
    }

    @Test
    public  void INexecute() {

        for (int i = 0; i < 100000; i++) {
            Random random = new Random();
            int xxxxxx = JdbcUtil.execute("INSERT delayed INTO `t3` ( `person_id`, `person_name`, `gmt_create`, `gmt_modified`) VALUES ( "+random.nextInt(10)+",'user_1000000"+i+"', '2033-07-24 16:16:18', '2039-04-20 19:56:12')");
        }


    }

    @Test
    public  void t() throws Exception {

        List<JdbcObj> sqls=new ArrayList<>();
        JdbcObj jdbcObj=new JdbcObj("select * from t_user where id=112", JdbcEnum.SELECT);
        sqls.add(jdbcObj);

        JdbcObj jdbcObj1=new JdbcObj("update t_user set username=?  where id=?",JdbcEnum.UPDATE,"xxxxxx1112");
        sqls.add(jdbcObj1);

        JdbTransactionBusTest jdbTransactionBus = new JdbTransactionBusTest(sqls);
        boolean transaction1 = JdbcUtil.transaction(jdbTransactionBus);
        System.out.println(transaction1);
    }
}
