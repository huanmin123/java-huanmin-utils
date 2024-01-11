package com.huanmin.utils.file_tool.mysql_log.conditionfilter.impl.analyse;


import com.huanmin.utils.file_tool.mysql_log.conditionfilter.ConditionFilterAnalyes;
import com.huanmin.utils.file_tool.mysql_log.entity.EplainEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SlowKeyConditionFilterSlowImpl implements ConditionFilterAnalyes {
    @Override
    public Map<String, Map<Integer, List<EplainEntity>>> condition(Map<String, Map<Integer, List<EplainEntity>>> map, EplainEntity eplainEntity) {
        if (eplainEntity.getSlowKey()!=null) {
            Map<String, Map<Integer, List<EplainEntity>>> map1 =new LinkedHashMap<>();
            for (Map.Entry<String, Map<Integer, List<EplainEntity>>> stringMapEntry : map.entrySet()) {
                Map<Integer, List<EplainEntity>> map2=new LinkedHashMap<>();
                for (Map.Entry<Integer, List<EplainEntity>> integerListEntry : stringMapEntry.getValue().entrySet()) {
                    List<EplainEntity> collect = integerListEntry.getValue().stream().filter((data) -> {
                        return Objects.equals(data.getSlowKey(), eplainEntity.getSlowKey());
                    }).collect(Collectors.toList());
                    map2.put(integerListEntry.getKey(),collect);
                }
                map1.put(stringMapEntry.getKey(),map2);
            }
            return  map1;
        }
        return  map;
    }
}
