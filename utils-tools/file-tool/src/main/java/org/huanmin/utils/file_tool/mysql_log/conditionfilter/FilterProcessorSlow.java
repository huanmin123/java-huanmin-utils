package org.huanmin.utils.file_tool.mysql_log.conditionfilter;

import org.huanmin.utils.file_tool.mysql_log.entity.SlowEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FilterProcessorSlow {

    @Autowired
    private List<ConditionFilterSlow> ConditionFilters;


    public    Map<String, List<SlowEntity>> Processor(Map<String, List<SlowEntity>> map, SlowEntity slowEntity){
        for (ConditionFilterSlow ConditionFilter : ConditionFilters) {
            map=ConditionFilter.condition(map,slowEntity);
        }
        return  map;
    }

}
