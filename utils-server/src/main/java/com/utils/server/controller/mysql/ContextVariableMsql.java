package com.utils.server.controller.mysql;


import com.utils.common.file.ResourceFileUtil;

//全局公共通用参数
public class ContextVariableMsql {
   public static String slowLogPath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/uploads-slowlog");
   public static String binLogPath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/uploads-binlog");
}
