package com.huanmin.utils.file_tool.mysql_log.dal.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huanmin.utils.file_tool.mysql_log.entity.EplainEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExplainSqlMapper extends BaseMapper<EplainEntity> {
    @Select("EXPLAIN  ${sql} ")
    List<EplainEntity> explain(String sql);

}
