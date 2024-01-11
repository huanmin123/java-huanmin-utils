package com.huanmin.utils.jdbc_extend.jdbc;

import java.sql.Connection;

public interface JdbTransaction {

    void run(Connection connection)throws Exception;

}
