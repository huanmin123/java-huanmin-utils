package org.huanmin.utils.file_tool.mysql_log.conditionfilter;


import org.huanmin.utils.file_tool.mysql_log.entity.EplainEntity;

import java.util.List;
import java.util.Map;

//条件过滤器
public interface ConditionFilterAnalyes {


    /**
     *
     * @param map  筛选的值
     * @param eplainEntity  筛选的条件
     * @return
     */
    Map<String, Map<Integer, List<EplainEntity>>> condition(Map<String, Map<Integer, List<EplainEntity>>> map, EplainEntity eplainEntity);

}
