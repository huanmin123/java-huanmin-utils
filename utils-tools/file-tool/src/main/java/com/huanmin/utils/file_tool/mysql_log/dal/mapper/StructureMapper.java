package com.huanmin.utils.file_tool.mysql_log.dal.mapper;

import com.huanmin.utils.file_tool.mysql_log.entity.TableStructureEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StructureMapper {

    @Select("SHOW COLUMNS FROM ${table}")
    List<TableStructureEntity> structureTable(String table);
}
