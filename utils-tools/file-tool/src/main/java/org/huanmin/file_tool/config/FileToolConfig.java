package org.huanmin.file_tool.config;

import org.huanmin.file_tool.email.MailServiceUtil;
import org.huanmin.file_tool.mysql_log.analyse.AnalyseSlowSql;
import org.huanmin.file_tool.mysql_log.binlog.BinLogHandle;
import org.huanmin.file_tool.mysql_log.conditionfilter.FilterProcessorAnalyse;
import org.huanmin.file_tool.mysql_log.conditionfilter.FilterProcessorSlow;
import org.huanmin.file_tool.mysql_log.conditionfilter.impl.analyse.SlowKeyConditionFilterSlowImpl;
import org.huanmin.file_tool.mysql_log.conditionfilter.impl.analyse.TypeConditionFilterSlowImpl;
import org.huanmin.file_tool.mysql_log.conditionfilter.impl.slow.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MailServiceUtil.class,
        AnalyseSlowSql.class,
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
        FilterProcessorSlow.class

})
public class FileToolConfig {
}
