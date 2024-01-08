package com.utils.file_tool.mysql_log.entity;

import lombok.Data;

@Data
public class SlowEntity {
    private  Integer key;  //唯一key
    private double lockTime; //锁的时间
    private double queryTime; //查询时间
    private int rowsSent; //sql执行后返回的行数
    private int rowsExamined; //sql执行扫描了多少行
    private String time; //什么时间段执行sql的时间
    private int id;
    private String user;  //用户
    private String sql;  //sql 语句
    private String fileName;  //文件名称



}
