package com.utils.server.controller.mysql;


import com.utils.common.spring.HttpJsonResponse;
import com.utils.file_tool.mysql_log.analyse.AnalyseSlowSql;
import com.utils.file_tool.mysql_log.conditionfilter.FilterProcessorAnalyse;
import com.utils.file_tool.mysql_log.entity.EplainEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("analyse")
public class AnalyseController {
    private static final Logger logger = LoggerFactory.getLogger(AnalyseController.class);
    @Autowired
    private AnalyseSlowSql analyseSlowSql;
    @Autowired
    private FilterProcessorAnalyse filterProcessorAnalyse;

    @GetMapping("analyseSlowSqlList")
    public HttpJsonResponse< Map<String, Map<Integer, List<EplainEntity>>>> analyseSlowSqlList(EplainEntity eplainEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(String.valueOf(eplainEntity));
        String path =ContextVariableMsql.slowLogPath;
        if (!new File(path).exists()) {
            return   HttpJsonResponse.success();
        } else {
            Map<String, Map<Integer, List<EplainEntity>>> stringMapMap = analyseSlowSql.toSlowEntitys(path);

            Map<String, Map<Integer, List<EplainEntity>>> processor = filterProcessorAnalyse.Processor(stringMapMap, eplainEntity);
            //如果map有key但是value都是null那么在序列化后,map就会消失 ,解决办法就是自己手动转为json格式,然后前端在进行JSON.parse(ref.data);转换为JSON对象进行操作
            return   HttpJsonResponse.success(processor);
        }
    }
}
