package org.huanmin.test.utils.utils_tools.file_tool.excel;



import org.huanmin.test.entity.UserEntity;
import org.huanmin.utils.file_tool.excel.ExcelUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HuAnmin
 * @version 1.0
 * @email 3426154361@qq.com
 * @date 2021/3/26-7:53
 * @description 类描述....
 */
public class ExcelUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtilsTest.class);
    @Test
    public void show111() {
        String tb_spec = ExcelUtils.getValue("26", 2, "tb_spec");
        System.out.println(tb_spec);

    }

    //获取一条对象信息   (如果有 多个id 相同那么 获取最后一个)
    @Test
    public void show3() throws IOException, ParseException {
        //通过 id 查询 一行
        Map<String, Object> map = ExcelUtils.selectById("39", "tb_spec");
        System.out.println(String.valueOf(map));

    }

    // 查询全部数据
    @Test
    public void show1() throws IOException, ParseException {
        List<Map<String, Object>> tb_sku = ExcelUtils.selectList("tb_sku");
        System.out.println(String.valueOf(tb_sku));

    }

    //修改一条数据     必须将外部的文件关闭 否则修改失败   (另一个程序正在使用此文件，进程无法访问。)
    //修改行的 每一列都要有数据  否则 默认你 将他清空 避免这种情况发生 解决办法
    // 简单来说就是不修改的数据 将原数据写上   觉得的麻烦你可以先将那一列的数据先查询出来    但是不要空值   (下面演示)
    @Test
    public void show4() throws IOException, ParseException {
        String id = "26";  //要修改数据行的 id
        Map<String, Object> map = ExcelUtils.selectById(id, "tb_spec"); //查询一条数据
        Map<String, Object> upmap = new HashMap<>();
        upmap.put("id", id);  //必须
        upmap.put("name", "尺码1");
        upmap.put("options", "162,175,176");
        upmap.put("seq", map.get("seq"));
        upmap.put("template_id", map.get("template_id"));
        ExcelUtils.update(upmap, "tb_spec"); //修改数据

    }


    //  注意插入时候 id不要相同否则数据就不好查询了
    @Test
    public void show5() throws IOException, ParseException {
        Map<String, Object> upmap = new HashMap<>();
        //id必须   使用你自己的id生成器 或者将 id存储在Redis缓存里设置永不过期  每次调用增加 的时候获取一次 然后id+1 这样你id永远不会重复
        upmap.put("id", 100);
        upmap.put("name", "xxx");
        upmap.put("options", "xxxx");
        upmap.put("seq", "xxxx");
        upmap.put("template_id", "xxxx");
        ExcelUtils.add(upmap, "tb_spec");
    }


    //注意如果id相同默认删除第一个出现的id  所以建议在添加的时候不要id相同
    //删除的原理就是将整行清空   在excel中最右边有一个搜索的小按钮 ->定位 选择空值->  然后鼠标在选择的上方右键 -> 删除行 这样就能快速的将空行删除了
    //这也是我们在上面修改和插入时候 一直强调不要有空值 的原因
    //空行对我么有影响吗?  对我们后端来说是没有影响的 查询会自动过滤的  但是不影响美观
    @Test
    public void show6() throws IOException, ParseException {
        ExcelUtils.delete("39", "tb_spec");
    }

    //获取列数
    @Test
    public void show78() throws IOException {
        int tb_spec = ExcelUtils.getColNum_xsl("tb_spec");
        System.out.println(String.valueOf(tb_spec));
    }

    //获取行数
    @Test
    public void show7() {
        int tb_spec = ExcelUtils.getRow("tb_spec");
        System.out.println(String.valueOf(tb_spec));
    }
    //获取最大的 id值 (注意如果你id不是有序的 那么就不要使用)
    @Test
    public void show() {
        int tb_spec = ExcelUtils.getMaxId(ExcelUtils.PATH, "tb_spec");
        System.out.println(String.valueOf(tb_spec));

    }


   //获取excel  表内 指定id 是否存在 如果存在 那么 返回 当前id 所在的条数  否则 -1
    @Test
    public void show11() {

        int tb_spec = ExcelUtils.getXH("38", "tb_spec");
        System.out.println(String.valueOf(tb_spec));

    }


    // 创建表

    @Test
    public void ss() {

        String excelFile = "C:\\UserDatas\\12841\\Desktop\\2020100913421411.xlsx";
        String sheetName = "abc";//表名
        String[] cellArray = {"id", "name", "age", "sex"};  //字段
        ExcelUtils.createExcel(excelFile, sheetName, cellArray);
    }
    // 反射的方式创建表
    @Test
    public void s31s() {
        ExcelUtils. createExcel("C:\\UserDatas\\12841\\Desktop\\20201009134214.xlsx",  UserEntity.class);
    }


    //反射创建表的同时插入数据
    @Test
    public void ss1() {

        //------创建表数据
        String excelFile = "C:\\UserDatas\\12841\\Desktop\\2020100913421411.xlsx";

        //------插入数据
        UserEntity UserData1= UserEntity.builder().id(1).age(22).sex("男").name("hu1").build();
        UserEntity UserData2= UserEntity.builder().id(2).age(23).sex("女").name("hu2").build();
        UserEntity UserData3= UserEntity.builder().id(3).age(24).sex("男").name("hu3").build();
        List<Object> list=new ArrayList<Object>(){
            {
                add(UserData1);
                add(UserData2);
                add(UserData3);
            }
        };
        ExcelUtils.excelCreateAndInsert(excelFile,list);

    }

}
