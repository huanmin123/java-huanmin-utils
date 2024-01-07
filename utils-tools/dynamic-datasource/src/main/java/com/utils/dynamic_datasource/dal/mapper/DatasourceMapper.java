package com.utils.dynamic_datasource.dal.mapper;


import com.utils.dynamic_datasource.entity.DataSourceEneity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DatasourceMapper  {
  List<DataSourceEneity> getDataSources();

}
