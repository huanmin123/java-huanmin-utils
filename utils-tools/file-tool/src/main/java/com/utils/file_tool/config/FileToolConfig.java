package com.utils.file_tool.config;

import com.utils.dynamic_datasource.aop.DynamicDataSourceAspect;
import com.utils.file_tool.email.MailServiceUtil;
import com.utils.file_tool.mysql_log.analyse.AnalyseSlowSql;
import com.utils.file_tool.mysql_log.binlog.BinLogHandle;
import com.utils.file_tool.mysql_log.conditionfilter.FilterProcessorAnalyse;
import com.utils.file_tool.mysql_log.conditionfilter.FilterProcessorSlow;
import com.utils.file_tool.mysql_log.conditionfilter.impl.analyse.SlowKeyConditionFilterSlowImpl;
import com.utils.file_tool.mysql_log.conditionfilter.impl.analyse.TypeConditionFilterSlowImpl;
import com.utils.file_tool.mysql_log.conditionfilter.impl.slow.*;
import com.utils.file_tool.mysql_log.dal.dao.impl.ExplainSqlDaoImpl;
import com.utils.file_tool.mysql_log.service.ExplainSqlServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        BinLogHandle.class,
        SlowKeyConditionFilterSlowImpl.class,
        TypeConditionFilterSlowImpl.class,
        FileNameConditionFilterSlowImpl.class,
        KeyConditionFilterSlowImpl.class,
        LockTimeConditionFilterSlowImpl.class,
        QueryTimeConditionFilterSlowImpl.class,
        RowsExaminedConditionFilterSlowImpl.class,
        RowsSentConditionFilterSlowImpl.class,
        TimeConditionFilterSlowImpl.class,
        UserConditionFilterSlowImpl.class,
        FilterProcessorAnalyse.class,
        FilterProcessorSlow.class,
        MailServiceUtil.class,
        AnalyseSlowSql.class,
        //业务代码
        ExplainSqlServiceImpl.class,
        ExplainSqlDaoImpl.class,
})
public class FileToolConfig {
}
