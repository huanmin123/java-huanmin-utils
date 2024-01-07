package org.huanmin.file_tool.mysql_log.binlog;

import com.utils.common.string.PatternCommon;
import org.apache.commons.lang.StringUtils;
import com.utils.common.file.FileUtil;
import com.utils.common.file.ReadFileLineUtil;
import com.utils.common.file.ResourceFileUtil;
import com.utils.common.file.WriteFileStrUtil;

import org.huanmin.file_tool.mysql_log.dao.StructureMapper;
import org.huanmin.file_tool.mysql_log.entity.TableStructureEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class BinLogHandle {
    private static final Logger logger = LoggerFactory.getLogger(BinLogHandle.class);
    private String pathFile;
    @Autowired
    private StructureMapper structureDao;

    Map<String, List<String>> map = new LinkedHashMap<String, List<String>>(100000) {{
        put("INSERT", new ArrayList<>());
        put("DELETE", new ArrayList<>());
        put("UPDATE", new ArrayList<>());
    }};
    List<String> listDML = new ArrayList<>(100000);
    List<String> structures = new ArrayList<>(100000);
    StringBuilder bingLog = new StringBuilder(100000);

    public BinLogHandle() {
    }

    // 取出增删改语句DML   (不含数据定义DDL语句和数据控制DCL语句)
    //这种初始化只支持,(单向恢复,恶意删除,恶意增加,恶意修改)
    private void initClassify(String pathFile) {
        this.pathFile = pathFile;
        AtomicReference<StringBuilder> stringBuilder = new AtomicReference<>(new StringBuilder());
        AtomicInteger onOff = new AtomicInteger();
        ReadFileLineUtil.readFileStrLine(new File(this.pathFile),(line) -> {
            initClassify(onOff, line, stringBuilder);
        });
    }

    public void initClassify(AtomicInteger onOff, String line, AtomicReference<StringBuilder> stringBuilder) {

        if (line.startsWith("###")) {
            onOff.set(1);
            //去掉###和@x=然后在添加
            String substring = line.substring(4);
            if (substring.trim().startsWith("@")) {
                List<String> list = PatternCommon.cutPatternStr(substring.trim(), "@\\d=(.*)", 1);
                stringBuilder.get().append(list.get(1)).append("&%#");
            } else {
                stringBuilder.get().append(substring).append(" ");
            }

        }
        if (onOff.get() == 1 && line.startsWith("# at")) {
            classify(stringBuilder.toString());
            stringBuilder.set(new StringBuilder());
            onOff.set(0);
        }
    }

    //数据分类
    private void classify(String sql) {
        if (sql.startsWith("INSERT")) {
            List<String> list = map.get("INSERT");
            list.add(sql);
        }
        if (sql.startsWith("DELETE")) {
            List<String> list = map.get("DELETE");
            list.add(sql);
        }
        if (sql.startsWith("UPDATE")) {
            List<String> list = map.get("UPDATE");
            list.add(sql);
        }
    }

    //这种初始化支持,全修复
    private void initDML(String pathFile) {
        this.pathFile = pathFile;
        AtomicReference<StringBuilder> stringBuilder1 = new AtomicReference<>(new StringBuilder());
        AtomicInteger onOff1 = new AtomicInteger();
        AtomicInteger onOff2 = new AtomicInteger();
        ReadFileLineUtil.readFileStrLine(new File(this.pathFile), (line) -> {
            initDML(onOff1, onOff2, line, stringBuilder1);
        });

    }

    public void initDML(AtomicInteger onOff, AtomicInteger onOff2, String line, AtomicReference<StringBuilder> stringBuilder) {

        if (line.startsWith("###")) {
            onOff.set(1);
            //去掉###和@x=然后在添加
            String substring = line.substring(4);
            if (substring.trim().startsWith("@")) {
                List<String> list = PatternCommon.cutPatternStr(substring.trim(), "@\\d=(.*)", 1);
                stringBuilder.get().append(list.get(1)).append("&%#");
            } else {
                stringBuilder.get().append(substring).append(" ");
            }
        }
        if (onOff.get() == 1 && line.startsWith("# at")) {
            listDML.add(stringBuilder.toString());
            stringBuilder.set(new StringBuilder());
            onOff.set(0);
        }

    }

    //只获取全部结构
    private void initDDL(String pathFile) {
        this.pathFile = pathFile;
        AtomicReference<StringBuilder> stringBuilder1 = new AtomicReference<>(new StringBuilder());
        AtomicInteger onOff1 = new AtomicInteger();
        AtomicInteger onOff2 = new AtomicInteger();
        ReadFileLineUtil.readFileStrLine(new File(this.pathFile), (line) -> {
            initDDL(onOff1, onOff2, line, stringBuilder1);
        });
        System.out.println(String.valueOf(structures));

    }

    private void initDDL(AtomicInteger onOff, AtomicInteger onOff2, String line, AtomicReference<StringBuilder> stringBuilder) {
        if (onOff2.get() == 1 && line.startsWith("/*!*/;")) {
            structures.add(stringBuilder.toString());
            stringBuilder.set(new StringBuilder());
            onOff2.set(0);
            onOff.set(0);
        }
        if (onOff.get() == 3 || line.startsWith("ALTER") || line.startsWith("CREATE")) {
            onOff2.set(1);
            onOff.set(3);
            if (line.contains("DROP")) {
                stringBuilder.set(new StringBuilder());
                onOff.set(0);
                onOff2.set(0);
            } else {
                stringBuilder.get().append(line).append(" ");
            }
        }
    }

    /**
     * @param pathFile 路径
     */
    public String cleanFormat(String pathFile) {
        this.pathFile = pathFile;

        ReadFileLineUtil.readFileStrLine(new File(this.pathFile), (line) -> {

            if (line.startsWith("/*") || (line.startsWith("#") && !line.startsWith("###")) || line.startsWith("SET")) {
                if (line.startsWith("COMMIT/*!*/;")) {
                    bingLog.append("\n");
                }

            } else {
                if (StringUtils.isBlank(line)) {
                    return;
                }
                String sql = line.replaceAll("/\\*!\\*/", "");
                if (sql.startsWith("###")) {
                    bingLog.append(sql.substring(4)).append("\n");
                } else {
                    bingLog.append(sql).append("\n");
                }
                if (sql.startsWith("COMMIT;")) {
                    bingLog.append("\n\n");
                }

            }

        });

        String mysqlRecoverSqlPath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/mysqlRecoverSql/" + "cleanFormat" + FileUtil.getGenerateFileName() + ".sql");
        WriteFileStrUtil.writeStrAppend(new File(mysqlRecoverSqlPath), bingLog.toString());
        return mysqlRecoverSqlPath;
    }


    private String insertVersion1(String sql) {
        String[] inserts = sql.trim().split("INSERT");
        if (inserts.length > 2) {
            List<String> list = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder(1000);
            for (int i = 1; i < inserts.length; i++) {
                list.add("INSERT" + inserts[i]);
            }
            for (String sql1 : list) {
                String insert = insert(sql1);
                stringBuilder.append(insert).append(";").append("\n");
            }
            return stringBuilder.toString();
        }
        return insert(sql);
    }

    @Deprecated
    private String insert(String sql) {
        List<String> list = PatternCommon.cutPatternStr(sql, "INSERT INTO (.*)SET([\\s\\S]*)", 1);
        if (list == null) {
            return null;
        }
        StringBuilder stringBuilder = null;
        try {
            String tableName = list.get(1);
            String[] values = list.get(2).trim().split("&%#");
            List<TableStructureEntity> structureTable = structureDao.structureTable(tableName);
            stringBuilder = new StringBuilder("INSERT INTO  " + tableName + "  SET ");
            for (int i = 0; i < values.length; i++) {
                if (i == values.length - 1) {
                    stringBuilder.append(" ").append(structureTable.get(i).getField()).append("=").append(values[i]);
                } else {
                    stringBuilder.append(" ").append(structureTable.get(i).getField()).append("=").append(values[i]).append(" ,");
                }
            }
        } catch (Exception e) {
            System.out.println(sql);
            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString();
    }

    private String delete(String sql) {
        List<String> list = PatternCommon.cutPatternStr(sql, "DELETE FROM (.*)WHERE([\\s\\S]*)", 1);
        if (list == null) {
            return null;
        }
        StringBuilder stringBuilder = null;
        try {
            String tableName = list.get(1);
            String[] values = list.get(2).trim().split("&%#");
            List<TableStructureEntity> structureTable = structureDao.structureTable(tableName);
            stringBuilder = new StringBuilder("DELETE FROM  " + tableName + "  WHERE ");
            for (int i = 0; i < values.length; i++) {
                if (i == values.length - 1) {
                    stringBuilder.append(" ").append(structureTable.get(i).getField()).append("=").append(values[i]);
                } else {
                    stringBuilder.append(" ").append(structureTable.get(i).getField()).append("=").append(values[i]).append(" and ");
                }
            }
        } catch (Exception e) {
            System.out.println(sql);
            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString();
    }

    private String update(String sql) {
        List<String> list = PatternCommon.cutPatternStr(sql, "UPDATE (.*)WHERE([\\s\\S]*)SET([\\s\\S]*)", 1);
        if (list == null) {
            return null;
        }
        StringBuilder stringBuilder = null;
        try {
            String tableName = list.get(1);
            String[] values1 = list.get(2).trim().split("&%#");
            String[] values2 = list.get(3).trim().split("&%#");
            List<TableStructureEntity> structureTable = structureDao.structureTable(tableName);
            stringBuilder = new StringBuilder("UPDATE " + tableName + " SET ");

            for (int i = 0; i < values1.length; i++) {
                if (i == values1.length - 1) {
                    stringBuilder.append(" ").append(structureTable.get(i).getField()).append("=").append(values2[i]);
                } else {
                    stringBuilder.append(" ").append(structureTable.get(i).getField()).append("=").append(values2[i]).append(" ,");
                }
            }
            stringBuilder.append(" WHERE ");
            for (int i = 0; i < values1.length; i++) {
                if (i == values1.length - 1) {
                    stringBuilder.append(" ").append(structureTable.get(i).getField()).append("=").append(values1[i]);
                } else {
                    stringBuilder.append(" ").append(structureTable.get(i).getField()).append("=").append(values1[i]).append(" and ");
                }
            }
        } catch (Exception e) {
            System.out.println(sql);
            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString();
    }

    private String delToIns(List<String> list) {
        StringBuilder strings = new StringBuilder(1000);
        for (String desql : list) {
            List<String> listp = PatternCommon.cutPatternStr(desql, "DELETE FROM (.*)WHERE([\\s\\S]*)", 1);
            if (listp == null) {
                return null;
            }
            String tableName = listp.get(1);
            String format = MessageFormat.format("INSERT INTO {0} SET {1}", tableName, listp.get(2));
            String insertVersion1 = insertVersion1(format);
            if (insertVersion1 != null) {
                strings.append(insertVersion1).append(";").append("\n");
            }
        }
        return strings.toString();
    }

    private String insTodelVersion1(List<String> list) {
        StringBuilder strings = new StringBuilder(1000);
        for (String insql : list) {
            String[] inserts = insql.trim().split("INSERT");
            if (inserts.length > 2) {
                List<String> list1 = new ArrayList<>();
                for (int i = 1; i < inserts.length; i++) {
                    list1.add("INSERT" + inserts[i]);
                }
                for (String sql1 : list1) {
                    List<String> listp = PatternCommon.cutPatternStr(sql1, "INSERT INTO (.*)SET([\\s\\S]*)", 1);
                    String tableName = listp.get(1);
                    String format = MessageFormat.format("DELETE FROM {0} WHERE {1}", tableName, listp.get(2));
                    String delete = delete(format);
                    if (delete != null) {
                        strings.append(delete).append(";").append("\n");
                    }
                }
            } else {
                insTodel(insql, strings);
            }
        }
        return strings.toString();
    }

    @Deprecated
    private void insTodel(String insql, StringBuilder strings) {
        List<String> listp = PatternCommon.cutPatternStr(insql, "INSERT INTO (.*)SET([\\s\\S]*)", 1);
        if (listp == null) {
            return;
        }
        String tableName = listp.get(1);
        String format = MessageFormat.format("DELETE FROM {0} WHERE {1}", tableName, listp.get(2));
        String delete = delete(format);
        if (delete != null) {
            strings.append(delete).append(";").append("\n");
        }

    }

    private String newUpToOlUp(List<String> list) {
        StringBuilder strings = new StringBuilder(1000);
        for (String upsql : list) {
            List<String> listp = PatternCommon.cutPatternStr(upsql, "UPDATE (.*)WHERE([\\s\\S]*)SET([\\s\\S]*)", 1);
            if (listp == null) {
                return null;
            }
            String tableName = listp.get(1);
            String format = MessageFormat.format("UPDATE {0} WHERE {1} SET {2}", tableName, listp.get(3), listp.get(2));
            String update = update(format);
            if (update != null) {
                strings.append(update).append(";").append("\n");
            }
        }
        return strings.toString();
    }

    // 只能支持表结构是完整的,只是数据被误删了或者被恶意删除了的情况
    // 处理语句 ->数据丢失恢复语句  (修改和增加不管  , 删除语句改为增加)
    public String recoveryDel(String pathFile) {
        initClassify(pathFile);
        String mysqlRecoverSqlPath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/mysqlRecoverSql/" + "recoveryDel" + FileUtil.getGenerateFileName() + ".sql");
        List<String> list = map.get("DELETE");
        WriteFileStrUtil.writeStrAppend(new File(mysqlRecoverSqlPath), delToIns(list));
        return  mysqlRecoverSqlPath;
    }

    // 只能支持表结构是完整的,只是数据被恶意修改了的情况,还原修改之前的数据
    public String recoveryUp(String pathFile) {
        initClassify(pathFile);
        String mysqlRecoverSqlPath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/mysqlRecoverSql/" + "recoveryUp" + FileUtil.getGenerateFileName() + ".sql");
        List<String> list = map.get("UPDATE");
        WriteFileStrUtil.writeStrAppend(new File(mysqlRecoverSqlPath), newUpToOlUp(list));
        return  mysqlRecoverSqlPath;
    }

    // 只能支持表结构是完整的,只是数据被恶意洪水攻击,恶意添加数据导致数据库的数据暴增,删除增加了的数据
    public String recoveryIns(String pathFile) {
        initClassify(pathFile);
        String mysqlRecoverSqlPath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/mysqlRecoverSql/" + "recoveryIns" + FileUtil.getGenerateFileName() + ".sql");
        List<String> list = map.get("INSERT");
        WriteFileStrUtil.writeStrAppend(new File(mysqlRecoverSqlPath), insTodelVersion1(list));
        return  mysqlRecoverSqlPath;
    }

    //数据库-表结构恢复
    public String recoveryStructures(String pathFile) {
        initDDL(pathFile);
        StringBuilder strings = new StringBuilder(10000);

        //结构恢复
        for (String structure : structures) {
            strings.append(structure).append(";").append("\n");

        }
        String mysqlRecoverSqlPath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/mysqlRecoverSql/" + "recoveryStructures" + FileUtil.getGenerateFileName() + ".sql");
        WriteFileStrUtil.writeStrAppend(new File(mysqlRecoverSqlPath), strings.toString());
        return  mysqlRecoverSqlPath;
    }

    //表和库必须存在 ,可以支持恢复表结构和数据   (先进行数据库和表结构恢复,然后在进行数据恢复,不然数据恢复会出现问题)
    public String recoveryData(String pathFile) {

        initDML(pathFile);
        StringBuilder strings = new StringBuilder(10000);
        //数据恢复
        for (String sql : listDML) {
            String insert = insertVersion1(sql);
            String delete = delete(sql);
            String update = update(sql);
            if (insert != null) {
                strings.append(insert).append(";").append("\n");
            } else if (delete != null) {
                strings.append(delete).append(";").append("\n");
            } else if (update != null) {
                strings.append(update).append(";").append("\n");
            } else {
                strings.append(sql).append(";").append("\n");
            }
        }
        String mysqlRecoverSqlPath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/mysqlRecoverSql/" + "recoveryData" + FileUtil.getGenerateFileName() + ".sql");
        WriteFileStrUtil.writeStrAppend(new File(mysqlRecoverSqlPath), strings.toString());
        return  mysqlRecoverSqlPath;
    }


    //处理语句 ->数据回滚 (反方向处理sql语句, 删除改为增加 ,增加改为删除, 修改改为之前的数据)
    //注意事项 使用回滚前必须进行结构增量恢复,否则会出现部分失败现象
    public String rollback(String pathFile) {
        initDML(pathFile);
        StringBuilder strings = new StringBuilder(10000);
        //倒序->回滚
        for (int i = listDML.size() - 1; i > 0; i--) {
            String sql = listDML.get(i);
            String insTodelVersion1 = insTodelVersion1(new ArrayList<>(Arrays.asList(sql)));
            if (!StringUtils.isBlank(insTodelVersion1)) {
                strings.append(insTodelVersion1);
            }
            String delToIns = delToIns(new ArrayList<>(Arrays.asList(sql)));
            if (!StringUtils.isBlank(delToIns)) {
                strings.append(delToIns);
            }
            String newUpToOlUp = newUpToOlUp(new ArrayList<>(Arrays.asList(sql)));
            if (!StringUtils.isBlank(newUpToOlUp)) {
                strings.append(newUpToOlUp);
            }
        }

        String mysqlRecoverSqlPath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/mysqlRecoverSql/" + "rollback" + FileUtil.getGenerateFileName() + ".sql");
        WriteFileStrUtil.writeStrAppend(new File(mysqlRecoverSqlPath), strings.toString());
        return  mysqlRecoverSqlPath;
    }

}
