package org.huanmin.test.utils.utils_common.base;

import com.jhlabs.image.GaussianFilter;

import org.huanmin.utils.common.file.ResourceFileUtil;
import org.huanmin.utils.common.base.UniversalException;
import org.huanmin.utils.common.media.img.ImgageHandleUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImgageHandleUtilsTest {

    @Test
    public void show1() throws FileNotFoundException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("img/a.jpg");
        ImgageHandleUtil.imgSize(file,200,200);
    }

    @Test
    public void show2() throws IOException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("img/a.jpg");
        // 使用高斯过滤器
        ImgageHandleUtil.imgFilter(file,new GaussianFilter(10));
    }

    // 自定义图片
    @Test
    public void show3() throws IOException {
        File file = new File(ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("img"+File.separator+"creator.jpg"));

        ImgageHandleUtil.creatorImgage(500,500,(img)->{

            //设置字体颜色黑色,宋体,大小20 ,加粗
            img.setColor(Color.BLACK);
            Font font = new Font("宋体", Font.ITALIC, 30);
            img.setFont(font);
            //添加文字
            img.drawString("胡安民", 200, 70);

            //画一个蓝色的横线
            img.setColor(Color.BLUE);
            img.drawLine(50,100,450,100);

            //添加一个图片
            try {
                File file1 = new File(ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("img"+File.separator+"a.jpg"));
                BufferedImage sourceImage = ImageIO.read(file1);
                Image add = sourceImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
                img.drawImage(add,150,200,null);
            } catch (IOException e) {
                 UniversalException.logError(e);
            }
            //添加一个空心矩形
            img.setColor(Color.BLUE);
            img.drawRect(50,300,50,50);


            //添加一个实心圆形
            img.setColor(Color.pink);
            img.fillOval(100,300,50,50);

            //手动绘图   ,画一个三角
            img.setColor(Color.GREEN);
            img.drawPolygon(new int[]{100,50,150},new int[]{20,50,50},3);


        },file);
    }

    @Test
    public void show4() throws IOException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("img/a.jpg");
        ImgageHandleUtil.ImgageWatermark(file,(img)->{
            //在上面写上淡灰色胡安民
            img.setColor(Color.decode("#eaeef1"));
            Font font = new Font("宋体", Font.BOLD, 20);
            img.setFont(font);
            img.drawString("胡安民", 390, 350);
            img.dispose();
        });
    }
}
