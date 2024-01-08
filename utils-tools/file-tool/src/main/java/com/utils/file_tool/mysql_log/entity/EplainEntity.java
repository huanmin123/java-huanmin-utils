package com.utils.file_tool.mysql_log.entity;

import lombok.Data;

@Data
public class EplainEntity {
    private Integer slowKey;// 慢日志唯一标识
    private int id;//标识符  id越大的就越先执行
    private String selectType;//表示查询的类型
    private String table;//输出结果集的表,查询的主表 ,有时不是真实的表名字，可能是简称
    private String partions;//匹配的分区
    private String type;//表示表的连接类型
    private String possibleKeys;//表示查询时，可能使用的索引字段名称
    private String key;//表示使用索引的字段名称
    private int keyLen;//索引字段的长度
    private String ref; //列与索引的比较
    private int rows; //返回的行数
    private double filtered;
    private String extra;
}
