package com.utils.file_tool.mysql_log.conditionfilter;

import com.utils.file_tool.mysql_log.entity.EplainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FilterProcessorAnalyse {

    @Autowired
    private List<ConditionFilterAnalyes> ConditionFilters;


    public     Map<String, Map<Integer, List<EplainEntity>>> Processor(Map<String, Map<Integer, List<EplainEntity>>> map, EplainEntity eplainEntity){
        for (ConditionFilterAnalyes ConditionFilter : ConditionFilters) {
            map=ConditionFilter.condition(map,eplainEntity);
        }
        return  map;
    }

}
