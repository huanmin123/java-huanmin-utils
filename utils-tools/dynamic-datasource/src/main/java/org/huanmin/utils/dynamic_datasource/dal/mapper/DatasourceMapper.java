package org.huanmin.utils.dynamic_datasource.dal.mapper;


import org.huanmin.utils.dynamic_datasource.entity.DataSourceEneity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DatasourceMapper  {
  List<DataSourceEneity> getDataSources();

}
