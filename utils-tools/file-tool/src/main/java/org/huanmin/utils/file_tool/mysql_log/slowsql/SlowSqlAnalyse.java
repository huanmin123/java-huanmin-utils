package org.huanmin.utils.file_tool.mysql_log.slowsql;


import org.huanmin.utils.file_tool.mysql_log.entity.SlowEntity;
import com.mchange.v1.util.SimpleMapEntry;

import org.huanmin.utils.common.base.DateUtil;
import org.huanmin.utils.common.base.LocalDateUtil;
import org.huanmin.utils.common.enums.DateEnum;
import org.huanmin.utils.common.string.PatternCommon;
import org.huanmin.utils.common.file.FileUtil;
import org.huanmin.utils.common.file.ReadFileLineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * # Time: 2022-03-15T08:15:40.687879Z  发生时间
 * # User@Host: root[root] @  [192.168.42.1]  Id:    92        用户
 * # Query_time: 0.000572  Lock_time: 0.000111 Rows_sent: 15  Rows_examined: 285
 * Query_time 请求时间
 * Lock_time  锁时间
 * Rows_sent  返回结果的行数
 * Rows_examined  扫描的行数
 */

public class SlowSqlAnalyse {
    private static final Logger logger = LoggerFactory.getLogger(SlowSqlAnalyse.class);
    private String pathFile;
    private Map<Integer, Map<String, String>> map = new HashMap<>();

    public Map<Integer, Map<String, String>> getMap() {
        return map;
    }

    public SlowSqlAnalyse(String pathFile) {
        this.pathFile = pathFile;
        //初始化所有sql语句
        init();
        filter();
        sortMapByValue();
    }

    // 读取3行 ,然后读取sql

    public void init() {
        AtomicReference<Map<String, String>> atomicReference = new AtomicReference<>();

        ReadFileLineUtil.readFileSkipStrLine(new File(this.pathFile), 3, (line) -> {
            //排除不需要的
            if (ExcludeDic.exclude(line, atomicReference)) {
                return;
            }
            int length = line.length();
            if (line.startsWith("#")) {

                Map<String, String> map = atomicReference.get();

                //截取时间
                if (line.startsWith("# Time: ")) {
                    //添加数据到列表中并清除上次数据
                    if (map != null) {
                        this.map.put(sqlIdHash(map), atomicReference.get());
                        atomicReference.set(new HashMap<>());//清除上次的痕迹
                    } else {
                        map = new HashMap<>();
                        atomicReference.set(map);
                    }

                    String tStr = LocalDateUtil.parseLocalDateTimeTStr(line.substring(8, length));
                    map.put(SlowEnum.TIME.getKey(), tStr);
                }
                //截取用户和id

                if (line.startsWith("# User@Host: ")) {
                    List<String> list = PatternCommon.cutPatternStr(line, "User@Host: (.*)\\s*Id:\\s*(\\d*)", 1);
                    try {
                        map.put(SlowEnum.USER.getKey(), list.get(1));
                        map.put("id", list.get(2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                //截取查询时间,锁时间,返回行数,扫描行数

                if (line.startsWith("# Query_time: ")) {
                    String sqsl = "# Query_time: 0.000816  Lock_time: 0.000086 Rows_sent: 15  Rows_examined: 288";
                    sqsl = PatternCommon.trimAll(line);
                    List<String> list = PatternCommon.cutPatternStr(sqsl, "#Query_time:(.*)Lock_time:(.*)Rows_sent:(.*)Rows_examined:(.*)", 1);
                    map.put(SlowEnum.QUERY_TIME.getKey(), list.get(1));
                    map.put(SlowEnum.LOCK_TIME.getKey(), list.get(2));
                    map.put(SlowEnum.ROWS_SENT.getKey(), list.get(3));
                    map.put(SlowEnum.ROWS_EXAMINED.getKey(), list.get(4));
                }

            } else {

                Map<String, String> map = atomicReference.get();
                //sql语句记录
                String sql = map.get(SlowEnum.SQL.getKey());
                if (sql == null) {
                    map.put(SlowEnum.SQL.getKey(), line);
                } else {
                    String sql1 = map.get(SlowEnum.SQL.getKey());
                    String line1 = sql1 + line;
                    map.put(SlowEnum.SQL.getKey(), line1);
                }
            }

        });


    }


    //过滤时间小于1秒的sql
    public void filter() {
        List<Integer> delId = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> integerMapEntry : this.map.entrySet()) {
            for (Map.Entry<String, String> stringStringEntry : integerMapEntry.getValue().entrySet()) {
                if (stringStringEntry.getKey().equals(SlowEnum.QUERY_TIME.getKey())) {
                    if (Double.parseDouble(stringStringEntry.getValue().trim()) < 1) {
                        delId.add(integerMapEntry.getKey());
                        break;
                    }
                }
            }
        }
        //删除
        for (Integer integer : delId) {
            map.remove(integer);
        }
    }


    //按照调用时间进行从小到大进行排序   (冒泡排序 -后期可以进行优化为快速排序)
    public void sortMapByValue() {
        Map<Integer, Map<String, String>> sortedMap = new LinkedHashMap<Integer, Map<String, String>>();
        List<Map.Entry<Integer, Map<String, String>>> entryList = new ArrayList<Map.Entry<Integer, Map<String, String>>>(this.map.entrySet());

        for (int i = 0; i < entryList.size(); i++) {
            String s1 = entryList.get(i).getValue().get(SlowEnum.TIME.getKey()).trim();
            for (int i1 = 0; i1 < entryList.size(); i1++) {
                String s2 = entryList.get(i1).getValue().get(SlowEnum.TIME.getKey()).trim();
                //按照时间,从大到小排序
                if (DateUtil.dateEq(s1, s2, DateEnum.DATETIME_PATTERN) == 1) {
                    //保留初始值  s1
                    SimpleMapEntry simpleMapEntry = new SimpleMapEntry(entryList.get(i).getKey(), entryList.get(i).getValue());
                    //s2放入s1位置
                    entryList.set(i, entryList.get(i1));
                    //s1放入s2位置
                    entryList.set(i1, simpleMapEntry);

                }
            }
        }
        //将排序结果从新填充
        for (Map.Entry<Integer, Map<String, String>> mapEntry : entryList) {
            sortedMap.put(mapEntry.getKey(), mapEntry.getValue());
        }
        this.map = sortedMap;
    }


    //生成唯一id
    public int sqlIdHash(Map<String, String> map) {
        map.get(SlowEnum.ROWS_EXAMINED.getKey());
        map.get(SlowEnum.SQL.getKey());
        int hash = Objects.hash(map.get(SlowEnum.TIME.getKey()), map.get(SlowEnum.USER.getKey()),
                map.get(SlowEnum.QUERY_TIME.getKey()), map.get(SlowEnum.LOCK_TIME.getKey()),
                map.get(SlowEnum.ROWS_SENT.getKey()), map.get(SlowEnum.ROWS_EXAMINED.getKey()),
                map.get(SlowEnum.ROWS_EXAMINED.getKey()),
                map.get(SlowEnum.SQL.getKey())
        );
        return hash;
    }


    public static Map<String, List<SlowEntity>> toSlowEntitys(String derPath) {
        Map<String, List<SlowEntity>> map=new LinkedHashMap<>();;
        List<String> filesAll = FileUtil.getFilesAll(derPath);
        for (String path : filesAll) {
            List<SlowEntity> list=new ArrayList<>();
            SlowSqlAnalyse slowSqlAnalyse = new SlowSqlAnalyse(path);
            Map<Integer, Map<String, String>> map1 = slowSqlAnalyse.getMap();
            for (Map.Entry<Integer, Map<String, String>> mapEntry : map1.entrySet()) {
                SlowEntity slowEntity=new SlowEntity();
                slowEntity.setKey(mapEntry.getKey());
                Map<String, String> value = mapEntry.getValue();
                slowEntity.setId(Integer.parseInt(value.get(SlowEnum.ID.getKey())));
                slowEntity.setLockTime(Double.parseDouble(value.get(SlowEnum.LOCK_TIME.getKey())));
                slowEntity.setQueryTime(Double.parseDouble(value.get(SlowEnum.QUERY_TIME.getKey())));
                slowEntity.setTime(value.get(SlowEnum.TIME.getKey()));
                slowEntity.setUser(value.get(SlowEnum.USER.getKey()));
                slowEntity.setRowsSent(Integer.parseInt(value.get(SlowEnum.ROWS_SENT.getKey())));
                slowEntity.setRowsExamined(Integer.parseInt(value.get(SlowEnum.ROWS_EXAMINED.getKey())));
                slowEntity.setSql(value.get(SlowEnum.SQL.getKey()));
                list.add(slowEntity);
            }
            map.put(new File(path).getName(),list);

        }


        return map;
    }


    public static void main(String[] args) throws ParseException, FileNotFoundException {

        Map<String, List<SlowEntity>> map = toSlowEntitys("D:\\project\\utils\\common-tools\\file-direction\\mysql-log\\target\\classes\\uploads");

        System.out.println(String.valueOf(map));

    }


}
