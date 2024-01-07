package org.huanmin.file_tool.mysql_log.slowsql;

public enum SlowEnum {
    TIME("time","什么时间点,执行的sql"),
    USER("user","用户信息"),
    ID("id","xx"),
    QUERY_TIME("query_time","执行sql耗时"),
    LOCK_TIME("lock_time","sql执行时候占用锁多久"),
    ROWS_SENT("rows_sent","sql执行后返回多少行语句"),
    ROWS_EXAMINED("rows_examined","执行sql时候扫描了多少条数据,这个越大代表性能越低(和索引有关系)"),
    SQL("sql","sql语句");



    private String key;
    private String dec;

    SlowEnum(String key, String dec) {
        this.key = key;
        this.dec = dec;
    }

    public String getKey() {
        return key;
    }
}
