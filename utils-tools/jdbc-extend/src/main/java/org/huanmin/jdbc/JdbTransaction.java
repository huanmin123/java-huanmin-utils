package org.huanmin.jdbc;

import java.sql.Connection;

public interface JdbTransaction {

    void run(Connection connection)throws Exception;

}
