package com.utils.file_tool.dal.mapper;

import com.utils.file_tool.mysql_log.entity.TableStructureEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StructureMapper {

    @Select("SHOW COLUMNS FROM ${table}")
    List<TableStructureEntity> structureTable(String table);
}
