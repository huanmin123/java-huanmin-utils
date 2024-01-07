package org.huanmin.file_tool.mysql_log.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.huanmin.file_tool.mysql_log.entity.TableStructureEntity;

import java.util.List;

@Mapper
public interface StructureMapper {

    @Select("SHOW COLUMNS FROM ${table}")
    List<TableStructureEntity> structureTable(String table);
}
