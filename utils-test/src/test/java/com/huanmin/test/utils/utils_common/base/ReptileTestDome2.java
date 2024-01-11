package com.huanmin.test.utils.utils_common.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.huanmin.utils.common.file.ResourceFileUtil;
import com.huanmin.utils.common.file.ReadFileLineUtil;
import com.huanmin.utils.common.file.WriteFileBytesUtil;
import com.huanmin.utils.common.requestclient.okhttp.OkHttpUtil;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * java  -Dfile.encoding=utf-8 -jar company.jar   启动这个jar 即可查询到所有公司信息
 * 读取 http://localhost:8080/company 网站华北地区一个星期的所有天气 ,然后保存到csv文件中
 * 将所有公司，部门，人员信息数据爬取下来，然后将数据写入到JSON文件result.json中。
 * <p>
 * result.json里有多个公司、部门、人员信息，
 * 需要提供一个交互功能，能根据编号查询出对应数据信息，
 * 输入的编号为公司，则显示所有公司信息，输入的编号为部门，
 * 则显示所有部门信息。输入编号为人员，则显示人员信息。
 * <p>
 * Jsoup可通过DOM，CSS以及类似于jQuery的操作方法来取出和操作数据。
 * 从一个URL，文件或字符串中解析HTML；
 * 使用DOM或CSS选择器来查找、取出数据；
 * 可操作HTML元素、属性、文本；
 */
public class ReptileTestDome2 {


    public static void crawlingToFile() throws FileNotFoundException {
        JSONArray jsonArray = new JSONArray();

        crawlingToFile("http://localhost:8080/company", jsonArray,null, 0);

        String absolutePath = ResourceFileUtil.getCurrentProjectAbsolutePath() + "/result.json";
        byte[] byte1 =jsonArray.toJSONString().getBytes();
        WriteFileBytesUtil.writeByte(byte1,new File(absolutePath),true);
    }

    public static void crawlingToFile(String url, JSONArray jsonArray,JSONObject jsonObject, int count) {

        //拿到网页的xml
        String doc = OkHttpUtil.builder()
                .url(url)
                .get()
                .sync();

        Document parse = Jsoup.parse(doc);

        Elements select = parse.select(".xwtable tr:nth-child(n+2)");
        for (int i = 0; i < select.size(); i++) {
            Elements select1 = select.get(i).select("td");
            Element element = select1.get(0);
            String href = element.select("a").attr("href");
            Element element1 = select1.get(1);
            switch (count) {
                case 0://公司
                    jsonObject = new JSONObject();
                    //添加公司
                    jsonObject.put("id", element.text());
                    jsonObject.put("name", element1.text());

                    JSONArray jsonArray1 = new JSONArray();
                    jsonObject.put("departments",jsonArray1);
                    //进行递归
                    crawlingToFile(href,jsonArray, jsonObject,1);
                    jsonArray.add(jsonObject);
                    break;
                case 1: //组织
                    JSONArray departments = jsonObject.getJSONArray("departments");

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id", element.text());
                    jsonObject1.put("name", element1.text());
                    JSONArray jsonArray2 = new JSONArray();
                    jsonObject1.put("employees",jsonArray2);
                    departments.add(jsonObject1);
                    //进行递归
                    crawlingToFile(href, departments,jsonObject1,2);
                    break;
                case 2://人员
                    //拿到组织
                    JSONArray employees = jsonObject.getJSONArray("employees");
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("id", element.text());
                    jsonObject2.put("name", element1.text());
                    employees.add(jsonObject2);
                    break;

            }


        }



    }




    @SneakyThrows
    public static void main(String[] args) {
        String absolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("result.json") ;

        if(absolutePath==null){
            crawlingToFile(); //爬取数据到result.json文件中
            absolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("result.json") ;
        }

        //读取文件内的数据到内存中
        assert absolutePath != null;
        String s = ReadFileLineUtil.readFileStrAll(new File(absolutePath));
        JSONArray jsonArray = JSON.parseArray(s);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("输入,公司id或者,部门id,员工id(查询对应信息): ");
            int i = scanner.nextInt();
            bak:
            for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i1);
                Integer id = jsonObject.getInteger("id");
                JSONArray departments = jsonObject.getJSONArray("departments");
                if (id==i) {
                    //公司下所有部门信息
                    System.out.println(departments);
                    break ;
                }
                for (int i2 = 0; i2 < departments.size(); i2++) {
                    JSONObject jsonObject1 = departments.getJSONObject(i2);
                    Integer id1 = jsonObject1.getInteger("id");
                    JSONArray employees = jsonObject1.getJSONArray("employees");
                    if (id1==i) {
                        //则显示该部门下所有员工信息
                        System.out.println(employees);
                        break bak;
                    }
                    for (int i3 = 0; i3 < employees.size(); i3++) {
                        JSONObject jsonObject2 = employees.getJSONObject(i3);
                        Integer id2 = jsonObject2.getInteger("id");
                        if (id2==i) {
                            //显示该员工的信息
                            System.out.println(jsonObject2);
                            break bak;
                        }
                    }


                }


            }

        }



    }

}
