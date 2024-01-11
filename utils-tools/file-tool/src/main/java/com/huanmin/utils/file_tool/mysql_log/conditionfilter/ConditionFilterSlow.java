package com.huanmin.utils.file_tool.mysql_log.conditionfilter;


import com.huanmin.utils.file_tool.mysql_log.entity.SlowEntity;

import java.util.List;
import java.util.Map;

//条件过滤器
public interface ConditionFilterSlow {


    /**
     *
     * @param map  筛选的值
     * @param slowEntity  筛选的条件
     * @return
     */
    Map<String, List<SlowEntity>> condition(Map<String, List<SlowEntity>> map, SlowEntity slowEntity);

}
