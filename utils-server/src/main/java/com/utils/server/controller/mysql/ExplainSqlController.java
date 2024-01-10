package com.utils.server.controller.mysql;

import com.utils.common.spring.HttpJsonResponse;
import com.utils.dynamic_datasource.aop.DBSwitch;
import com.utils.file_tool.mysql_log.entity.EplainEntity;
import com.utils.file_tool.mysql_log.service.ExplainSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author huanmin
 * @date 2024/1/10
 */
@RestController
@RequestMapping("mysql-explain-sql")
public class ExplainSqlController {

    @Autowired
    private ExplainSqlService explainSqlService;

    //http://localhost:12345/explain-sql
    //{sql: "select * from user"}
    @PostMapping("")
    public HttpJsonResponse<List<EplainEntity>> explainSql(@RequestBody HashMap<String,String>sql)  {
        List<EplainEntity> explain = explainSqlService.explain(sql.get("sql"));
        //获取当前目录下全部文件
        return HttpJsonResponse.success(explain);
    }



}
