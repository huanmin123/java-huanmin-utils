package com.utils.dynamic_datasource.dal.mapper;


import com.utils.dynamic_datasource.entity.DataSourceEneity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DatasourceDao  {
  @Select("SELECT * FROM t_datasource WHERE state= 1") //只查询可用的数据源
  List<DataSourceEneity> getDataSources();

}
