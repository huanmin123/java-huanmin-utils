package org.huanmin.test.utils.utils_common.base;

import org.huanmin.utils.common.base.ChromeDriverUtil;
import org.huanmin.utils.common.file.ResourceFileUtil;
import org.huanmin.utils.common.file.WriteFileStrUtil;
import org.huanmin.utils.common.requestclient.okhttp.OkHttpUtil;
import org.huanmin.utils.common.string.PatternCommon;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 读取 http://www.weather.com.cn/textFC/hb.shtml 网站华北地区一个星期的所有天气 ,然后保存到csv文件中
 * <p>
 * Jsoup可通过DOM，CSS以及类似于jQuery的操作方法来取出和操作数据。
 * 从一个URL，文件或字符串中解析HTML；
 * 使用DOM或CSS选择器来查找、取出数据；
 * 可操作HTML元素、属性、文本；
 */
public class ReptileTestDome1 {
    public ReptileTestDome1() {
    }


    @Test
    public void show11() {
        ChromeDriverUtil build = ChromeDriverUtil.build("D:\\常用资源\\chromedriver.exe");
        ChromeDriver driver = build.getDriver();
        driver.navigate().to("https://www.bilibili.com/anime");

        //找到元素,以渲染完毕
        WebElement elementa = driver.findElement(By.cssSelector(".timeline-toggle-btn .timeline-icon-toggle-down"));
        //移动到需要点击的元素位置 , 他这网站的事件是动态绑定的,也就是懒加载形式,元素出现在可视化窗口后对应的js相关的事件才会绑定上去
        driver.executeScript("window.scrollTo(0,arguments[0].getBoundingClientRect().top-300);", elementa);
        //给js留点时间绑定到元素上
        build.sleep(100);
        //展开下拉列表
        elementa.click();
        //获取需要滚动的距离
        Long o = (Long) driver.executeScript("let elementsByClassNameElement = document.querySelector(arguments[0]);\n" +
                        " return elementsByClassNameElement.offsetHeight+elementsByClassNameElement.offsetTop;",
                ".timeline-box.clearfix");
        //获取当前滚动条位置
        Long o1 = (Long) driver.executeScript("return document.documentElement.scrollTop;");
        // 向下移动滚动条让列表的内容懒加载出来
        for (Long i = o1; i < o; i += 500) {
            driver.executeScript("window.scrollTo(0,arguments[0]);", i);
            build.sleep(50);
        }

        WebElement element1 = driver.findElement(By.cssSelector(".timeline-box.clearfix"));
        List<WebElement> elements = element1.findElements(By.className("timeline-item"));
        for (WebElement webElement : elements) {
            //如果没有显示那么10秒内,每间隔500毫秒重新获取一次
            WebElement wait1 = build.wait(10, (driver1) -> {
                WebElement element = webElement.findElement(By.cssSelector(".common-lazy-img img"));
                if (element.getAttribute("src").isEmpty()) {
                    return null; //如果值为空那么,继续查询
                }
                return element;
            });
            System.out.println(wait1.getAttribute("src"));
        }
        driver.quit();
    }

    public static void main(String[] args) {
        ChromeDriverUtil build = ChromeDriverUtil.build("D:\\常用资源\\chromedriver.exe");
        ChromeDriver driver = build.getDriver();
        Map<String,List<String>> map=new LinkedHashMap<>();
        try {
            driver.navigate().to("https://www.bilibili.com/bangumi/play/ep541073?spm_id_from=333.1007.partition_recommend.content.click");
            //拿到父级
            WebElement element= driver.findElement(By.cssSelector(".comm .bb-comment .comment-list"));;
            //移动滚动条,到底部
            Long len = (Long) driver.executeScript("return document.body.scrollHeight;");
            while (true) {
                //移动滚动条
                driver.executeScript(" window.scrollTo(0,arguments[0])", len);
                build.sleep(200);
                Long len1 = (Long) driver.executeScript("return document.body.scrollHeight;");
                if (Objects.equals(len, len1)) {
                    break;
                }
                len = len1;
            }

            List<WebElement> elements = element.findElements(By.xpath("//div[@mr-show]"));
            for (WebElement webElement : elements) {
                WebElement element1 = webElement.findElement(By.cssSelector(".con .text"));
                map.put(element1.getText(), new ArrayList<>());
                WebElement element2 = webElement.findElement(By.cssSelector(".con .reply-box"));
                if(!element2.getText().isEmpty()){
                    List<WebElement> elements1 = element2.findElements(By.className("reply-item"));
                    List<String> list = map.get(element1.getText());
                    list.addAll(elements1.stream().map(data->data.findElement(By.cssSelector(".user .text-con")).getText()).collect(Collectors.toList()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); //无论啥情况必须释放资源
        }

        //遍历数据
        for (Map.Entry<String, List<String>> stringListEntry : map.entrySet()) {
            System.out.println(stringListEntry);
            for (String s : stringListEntry.getValue()) {
                System.out.println("--:"+s);
            }

        }

    }

    @Test
    public void show() {
        //拿到网页的xml
        String doc = OkHttpUtil.builder()
                .url("http://www.weather.com.cn/textFC/hb.shtml")
                .get()
                .sync();

        //时间,省市,城市,天气现象
        StringBuilder stringBuilder0 = new StringBuilder();
        //标题
        StringBuilder stringBuilder = new StringBuilder("时间,省市,城市,天气现象,风向风力,最高气温,天气现象,风向风力,最低气温");
        stringBuilder0.append(stringBuilder).append("\n");
        Document document = Jsoup.parse(doc);//将页面转换为Document
        //使用css 选择器
        Elements selecttop = document.select(".day_tabs li");

        //拿到数据列表
        Elements select = document.select(".conMidtab");
        for (int i1 = 0; i1 < selecttop.size(); i1++) {
            Element element = selecttop.get(i1);

            String text = element.text();
            //进行数据清洗,取出时间
            String time = PatternCommon.cutPatternStr(text, "[\\u4e00-\\u9fa5]*\\((\\S*)\\)", 1).get(1);
            //取和实际对应的列表
            Element element1 = select.get(i1);
            Elements midtab = element1.select(".conMidtab2");
            for (int i = 0; i < midtab.size(); i++) {
                StringBuilder stringBuilder1 = new StringBuilder();
                //时间
                stringBuilder1.append(time).append(",");
                Element element2 = midtab.get(i);
                //拿到所有的行
                Elements trs = element2.select("table tr");
                //拿到省市
                Elements select2 = trs.select(".rowspan");
                stringBuilder1.append(select2.text()).append(",");
                //跳过前3行从第4行开始读取
                for (int i2 = 3; i2 < trs.size() - 1; i2++) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    Element element3 = trs.get(i2);
                    //拿到行下所有列  城市,天气现象,风向风力,最高气温,天气现象,风向风力,最低气温
                    Elements td = element3.select("td");
                    for (int i3 = 0; i3 < td.size(); i3++) {
                        Element element4 = td.get(i3);
                        if (i3 == td.size() - 2) {
                            //最后一个不需要逗号
                            stringBuilder2.append(element4.text());
                            break;
                        }
                        stringBuilder2.append(element4.text()).append(",");
                    }
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(stringBuilder1).append(stringBuilder2);
                    stringBuilder0.append(stringBuilder3).append("\n");
                }
            }
        }

        //将内容按行写入到csv文件中
        String absoluteFilePathAndCreate = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("/weather.csv");
        WriteFileStrUtil.writeStrCover(new File(absoluteFilePathAndCreate), stringBuilder0.toString());

    }


}
