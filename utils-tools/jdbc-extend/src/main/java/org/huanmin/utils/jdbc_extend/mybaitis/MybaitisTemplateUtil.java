package org.huanmin.utils.jdbc_extend.mybaitis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.util.Strings;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huanmin
 * @date 2024/1/3
 */
@Component
@Slf4j
public class MybaitisTemplateUtil {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    private final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //    开启链接
    private SqlSession openSession() {
        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        return sqlSessionFactory.openSession();
    }

    //    关闭链接
    private void closeSession(SqlSession sqlSession) {
        sqlSession.close();
    }

    //查询数量
    public int count(String sql, Object... params) throws SQLException {
        sql = sqlToCount(sql); //sql语句转count(1)
        sql = emptying(sql);
        log.info("old-count-sql:{}", sql);
        String format = format(sql, params);
        log.info("new-count-sql:{}", format);
        SqlSession sqlSession = openSession();
        try {
            PreparedStatement preparedStatement = sqlSession.getConnection().prepareStatement(format);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new RuntimeException("count-没有数据返回,查询失败,请检测sql:" + sql);
            }
        } finally {
            closeSession(sqlSession);
        }
    }

    /**
     * 支持: 字符串,8大数据类型,LocalDate,LocalDateTime,Date,List<8大数据类型> ,Map<String,8大数据类型>
     * 8大数据类型包括(和包装类型): byte,short,int,long,float,double,char,boolean
     * 数值类型不会加上单引号,其他类型都会加上单引号. 不支持自定义类型
     *
     * @param sql    sql语句
     * @param params 参数
     * @return
     */
    private String format(String sql, Object... params) {
        List<Object> collect = Arrays.stream(params).collect(Collectors.toList());
        for (int i = 0; i < collect.size(); i++) {
            if (collect.get(i) == null) {
                throw new RuntimeException("参数不能为null,请检第[" + i + "]个参数:" + collect.get(i));
            }
            Object transition = transition(collect.get(i));
            if (transition != null) {
                collect.set(i, transition);
                continue;
            }
            //如果是map,那么需要将{key}替换为'value'
            if (collect.get(i) instanceof Map) {
                Map<String, Object> param = (Map<String, Object>) collect.get(i);
                for (Map.Entry<String, Object> stringObjectEntry : param.entrySet()) {
                    sql = sql.replace("{" + stringObjectEntry.getKey() + "}", transition(stringObjectEntry.getValue()).toString());
                }
                //删除params中的map
                collect.remove(i);
            }
        }


        //将{1},{2}这种先进行替换,不然会导致String.format报错
        for (int i = 0; i < collect.size(); i++) {
            sql = sql.replace("{" + (i + 1) + "}", collect.get(i).toString());
        }

        return String.format(sql.replaceAll("\\{\\s*\\}", "%s"), collect.toArray());
    }

    //sql语句转count(1)
    private String sqlToCount(String sql) {
        //检测是否存在count(
        if (!sql.toLowerCase().contains("count(")) {//如果不存在,那么自动将select到from之间的内容替换为count(1)
            int selectIndex = sql.toLowerCase().indexOf("select");
            int fromIndex = sql.toLowerCase().indexOf("from");
            if (selectIndex == -1 || fromIndex == -1) {
                throw new RuntimeException("count-没有找到select或者from,请检测sql:" + sql);
            }
            selectIndex += 6;
            String substring = sql.substring(selectIndex, fromIndex);
            sql = sql.replace(substring, " count(1) ");

        }
        return sql;
    }


    //特殊符号处理
    private String specificSymbol(Object obj) {
        if (obj instanceof String) {
            //判断obj中是否存在有%的字符
            if (obj.toString().contains("%")) {//将所有的%替换为%%
                obj = ((String) obj).replace("%", "%%");
            }
            return obj.toString();
        } else {
            throw new RuntimeException("只能是字符串格式的才能进行特殊字符转义处理");
        }
    }

    private Object transition(Object obj) {
        if (obj == null) {
            throw new RuntimeException("参数不能为null,请检参数");
        }
        //如果是字符串,那么需要加上单引号
        if (obj instanceof String) {
            if (Strings.isBlank(obj.toString())) {
                throw new RuntimeException("参数不能为''空字符串,请检参数:" + obj);
            }
            //排除特殊字符
            obj = specificSymbol(obj);
            return "'" + obj + "'";
        }

        //如果是数值类型,那么不需要加上单引号
        if (obj instanceof Number) {
            return obj.toString();
        }
        //如果是char和boolean(包括包装类型),那么强制转换为String,然后加上单引号
        if (obj instanceof Character || obj instanceof Boolean) {
            return "'" + obj + "'";
        }

        //如果是List,需要转换为('1','2','3')这种格式
        if (obj instanceof List) {
            return listToBrackets((List) obj);
        }
        //如果是LocalDate,需要转换为'2020-01-01'这种格式
        if (obj instanceof LocalDate) {
            LocalDate param = (LocalDate) obj;
            return "'" + param.format(LOCAL_DATE_FORMAT) + "'";
        }
        //如果是LocalDateTime,需要转换为'2020-01-01 00:00:00'这种格式
        if (obj instanceof java.time.LocalDateTime) {
            LocalDate param = (LocalDate) obj;
            return "'" + param.format(LOCAL_DATE_TIME_FORMAT) + "'";
        }
        //如果是Date,需要转换为'2020-01-01 00:00:00'这种格式
        if (obj instanceof java.util.Date) {
            java.util.Date param = (java.util.Date) obj;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "'" + df.format(param) + "'";
        }
        return null;
    }

    private <T> String listToBrackets(List<T> list) {
        return list.stream().map(String::valueOf).collect(Collectors.joining("','", "('", "')"));
    }

    private String  emptying(String sql){
        //将所有{}之间的空格去掉,不然会导致String.format报错
        sql = sql.replaceAll("\\{\\s*\\}", "{}");
        //将sql中的\n替换为空,不然会导致sql执行失败
        sql = sql.replaceAll("\n", " ");
        //将sql中的多个空格替换为一个空格,不然打印出来的sql会很难看
        sql = sql.replaceAll("\\s+", " ");

        //将所有{xx} 把中间的xx取出来然后清除{}之间的空格,之后再替换为{xx}
        List<String> collect1 = Arrays.stream(sql.split("\\{")).map(s -> {
            if (s.contains("}")) {
                String[] split = s.split("\\}");
                if (split.length == 1) {
                    return split[0].replaceAll("\\s*", "") + "}";
                }
                if (split.length == 2) {
                    return split[0].replaceAll("\\s*", "") + "}" + split[1];
                }
            }
            return s;
        }).collect(Collectors.toList());
        sql = String.join("{", collect1);
        return sql;
    }

}
