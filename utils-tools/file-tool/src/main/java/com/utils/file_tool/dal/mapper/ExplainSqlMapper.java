package com.utils.file_tool.dal.mapper;


import com.utils.file_tool.mysql_log.entity.EplainEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExplainSqlMapper {
    @Select("EXPLAIN  ${sql} ")
    List<EplainEntity> explain(String sql);

}
