package com.huanmin.utils.file_tool.mysql_log.analyse;

import com.huanmin.utils.file_tool.mysql_log.dal.mapper.ExplainSqlMapper;
import com.huanmin.utils.file_tool.mysql_log.entity.EplainEntity;
import com.huanmin.utils.file_tool.mysql_log.entity.SlowEntity;
import com.huanmin.utils.file_tool.mysql_log.slowsql.SlowSqlAnalyse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnalyseSlowSql {

    @Autowired
    private ExplainSqlMapper explainSqlMapper;

    /**
     *   map 的key 就是SlowSqlAnalyse结果集中sql详情的唯一表示key
     * @param derPath 传入慢日志文件目录
     * @return
     */
    public  Map<String, Map<Integer,List<EplainEntity>>> toSlowEntitys(String derPath) {

        Map<String, Map<Integer,List<EplainEntity>>>  map1=new LinkedHashMap<>();
        Map<String, List<SlowEntity>> map = SlowSqlAnalyse.toSlowEntitys(derPath);
        for (Map.Entry<String, List<SlowEntity>> stringListEntry : map.entrySet()) {
            Map<Integer,List<EplainEntity>> map2=new LinkedHashMap<>();
            for (SlowEntity slowEntity : stringListEntry.getValue()) {
                List<EplainEntity> explain = explainSqlMapper.explain(slowEntity.getSql());
                for (EplainEntity eplainEntity : explain) {
                    eplainEntity.setSlowKey(slowEntity.getKey());
                }

                map2.put(slowEntity.getKey(),explain);
            }
            map1.put(stringListEntry.getKey(),map2);
        }
        return map1;
    }
}
