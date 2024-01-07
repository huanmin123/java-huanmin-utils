package org.huanmin.file_tool.mysql_log.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.huanmin.file_tool.mysql_log.entity.EplainEntity;

import java.util.List;

@Mapper
public interface ExplainSqlMapper {
    @Select("EXPLAIN  ${sql} ")
    List<EplainEntity> explain(String sql);

}
