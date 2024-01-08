package com.utils.jdbc_extend.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.util.Arrays;


public class JdbcObj {
    private static final Logger logger = LoggerFactory.getLogger(JdbcObj.class);
    private String sql;
    private Object[] args;
    private JdbcEnum type;
    private PreparedStatement preparedStatement;

    public JdbcObj(String sql, JdbcEnum type, Object... args) {
        this.sql = sql;
        this.args = args;
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getArgs() {
        return args;
    }

    public JdbcEnum getType() {
        return type;
    }

    public void setArgs(Object... args) {
        Object[] src=new Object[this.args.length+args.length];
       System.arraycopy( this.args, 0,src,0,this.args.length);
       System.arraycopy( args, 0,src, this.args.length,args.length);
      this.args=src;
    }

    public static void main(String[] args) {
        JdbcObj jdbcObj=new JdbcObj("",JdbcEnum.UPDATE,"22",33);

        jdbcObj.setArgs(1,3,4,5);

        System.out.println(Arrays.toString(jdbcObj.args));

    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }
}
