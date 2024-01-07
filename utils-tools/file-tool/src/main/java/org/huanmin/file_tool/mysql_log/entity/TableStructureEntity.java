package org.huanmin.file_tool.mysql_log.entity;

import lombok.Data;

@Data
public class TableStructureEntity {
    private  String field;
    private  String type;
    private  String key;
    private  String default_;
    private  String extra;
    public String getDefault() {
        return default_;
    }
    public void setDefault(String aDefault) {
        default_ = aDefault;
    }
}
