package org.huanmin.dynamic_datasource.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.huanmin.dynamic_datasource.entity.DataSourceEneity;

import java.util.List;

@Mapper
public interface DatasourceDao {
  @Select("SELECT * from t_datasource")
  List<DataSourceEneity> getDataSources();

}
