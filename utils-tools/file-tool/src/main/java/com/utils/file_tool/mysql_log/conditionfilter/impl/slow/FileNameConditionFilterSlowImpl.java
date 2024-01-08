package com.utils.file_tool.mysql_log.conditionfilter.impl.slow;


import com.utils.file_tool.mysql_log.conditionfilter.ConditionFilterSlow;
import com.utils.file_tool.mysql_log.entity.SlowEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FileNameConditionFilterSlowImpl implements ConditionFilterSlow {


    @Override
    public Map<String, List<SlowEntity>> condition(Map<String, List<SlowEntity>> map, SlowEntity slowEntity) {
        if (StringUtils.isNotBlank(slowEntity.getFileName())) {
            Map<String, List<SlowEntity>> map1 =new HashMap<>();
            for (Map.Entry<String, List<SlowEntity>> stringListEntry : map.entrySet()) {
                if (stringListEntry.getKey().equals(slowEntity.getFileName())) {
                    map1.put(stringListEntry.getKey(),stringListEntry.getValue());
                    break;
                }
            }
            return  map1;
        } else {
            return map;
        }
    }
}
