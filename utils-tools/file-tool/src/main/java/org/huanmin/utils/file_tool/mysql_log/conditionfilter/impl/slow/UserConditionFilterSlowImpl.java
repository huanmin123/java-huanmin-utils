package org.huanmin.utils.file_tool.mysql_log.conditionfilter.impl.slow;


import org.huanmin.utils.file_tool.mysql_log.conditionfilter.ConditionFilterSlow;
import org.huanmin.utils.file_tool.mysql_log.entity.SlowEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserConditionFilterSlowImpl implements ConditionFilterSlow {

    
    @Override
    public Map<String, List<SlowEntity>> condition(Map<String, List<SlowEntity>> map, SlowEntity slowEntity) {
        if (StringUtils.isNotBlank(slowEntity.getUser())) {
            Map<String, List<SlowEntity>> map1 =new HashMap<>();
            for (Map.Entry<String, List<SlowEntity>> stringListEntry : map.entrySet()) {
                 List<SlowEntity> collect = stringListEntry.getValue().stream().filter((data) -> {
                    return data.getUser().equals(slowEntity.getUser());
                }).collect(Collectors.toList());
                map1.put(stringListEntry.getKey(),collect);
            }
            return  map1;
        } else {
            return map;
        }
    }
}
