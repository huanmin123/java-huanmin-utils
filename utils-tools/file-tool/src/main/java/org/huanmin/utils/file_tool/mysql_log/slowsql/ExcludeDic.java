package org.huanmin.utils.file_tool.mysql_log.slowsql;


import org.huanmin.utils.common.string.PatternCommon;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ExcludeDic {
    public static final String TIMESTAMP = "^SET timestamp.*";
    public static final String USE = "^use.*";
    public static final String QUERY_ID = "SELECT QUERY_ID, SUM\\(DURATION\\) AS SUM_DURATION FROM INFORMATION_SCHEMA.PROFILING GROUP BY QUERY_ID;";
    public static final String STATE = "SELECT STATE AS `Status`, ROUND\\(SUM\\(DURATION\\),\\d*\\) AS `Duration`, CONCAT\\(ROUND\\(SUM\\(DURATION\\)/.*\\*\\d*,\\d*\\), .*\\) AS `Percentage` FROM INFORMATION_SCHEMA.PROFILING WHERE QUERY_ID=\\d* GROUP BY SEQ, STATE ORDER BY SEQ;";


        public  static boolean exclude(String line, AtomicReference<Map<String, String>> atomicReference) {

            if ( PatternCommon.isPatternAll(line, TIMESTAMP)) {
                return true;//跳过
            }

            if ( PatternCommon.isPatternAll(line, USE)) {
                return true;//跳过
            }
            if ( PatternCommon.isPatternAll(line, QUERY_ID)) {
                atomicReference.set(null);//清空数据,不保存
                return true;
            }
            if ( PatternCommon.isPatternAll(line, STATE)) {
                atomicReference.set(null );//清空数据,不保存
                return true;
            }



            return false;
        }



}
