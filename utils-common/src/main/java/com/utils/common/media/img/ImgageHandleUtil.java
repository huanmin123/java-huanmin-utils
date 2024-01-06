package com.utils.common.media.img;

import com.jhlabs.image.AbstractBufferedImageOp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class ImgageHandleUtil {


    /**
     *  图片大小调整  (分辨率)
     * @param file    需要调整的图片
     * @param width   将图片调整多宽
     * @param height  将图片调整多高
     */
    public static void imgSize(File file,int width, int height){
        try {
            //BufferedImage是Image与图像数据的访问的缓冲器
            BufferedImage image = ImageIO.read(file);

            //创建此图像的缩放版本。 返回一个新的Image对象
            Image scaledInstance = image.getScaledInstance(Double.valueOf(width ).intValue(), Double.valueOf(height ).intValue(), Image.SCALE_DEFAULT);
            //确定图像的宽度。 如果宽度未知，则此方法返回-1并稍后通知指定的ImageObserver对象
            int swidth = scaledInstance.getWidth(null);
            int sheight = scaledInstance.getHeight(null);

            BufferedImage newImage = new BufferedImage(swidth, sheight,
                    BufferedImage.TYPE_INT_ARGB);
            //Graphics对象封装了 Java 支持的基本渲染操作所需的状态信息,可以理解为是一支画笔
            Graphics g = newImage.getGraphics();
            //从坐标0,0处画一个图像
            g.drawImage(scaledInstance, 0, 0, null);
            //处理此图形上下文并释放它正在使用的任何系统资源。 调用dispose后无法使用Graphics对象。
            g.dispose();
            String newimg= file.getParent()+File.separator+"size-"+file.getName();
            //将新的图片信息写入scaled.png
            ImageIO.write(newImage, "png", new File(newimg));

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * http://www.jhlabs.com/ip/filters/index.html  滤镜大全
     * @param file
     * @param abstractBufferedImageOp   滤镜实现类  ,InvertFilter invertFilter = new InvertFilter(); (反转)  ...
     * @throws IOException
     */
    public static  void imgFilter(File file , AbstractBufferedImageOp abstractBufferedImageOp) throws IOException {

        BufferedImage sourceImage = ImageIO.read(file);
        Image scaledInstance = sourceImage.getScaledInstance(sourceImage.getWidth(), sourceImage.getHeight(), Image.SCALE_DEFAULT);
        BufferedImage newImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(scaledInstance, 0, 0, null);
        g.dispose();
        //滤镜
        abstractBufferedImageOp.filter(sourceImage, newImage);
        String newimg= file.getParent()+File.separator+"filter-"+file.getName();
        ImageIO.write(newImage, "png", new File(newimg));
    }
    public static void creatorImgage(int width, int height, Consumer<Graphics> consumer,File imgNamePath) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        //创建一个矩形当做背景色(白色)
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        consumer.accept(g);
//        释放资源
        g.dispose();
        ImageIO.write(bufferedImage, "png", imgNamePath);
    }

    //图片加水印
    public static  void ImgageWatermark(File file,Consumer<Graphics> consumer) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        Graphics g = bufferedImage.getGraphics();
        consumer.accept(g);
        String newimg= file.getParent()+File.separator+"watermark-"+file.getName();
        ImageIO.write(bufferedImage, "png", new File(newimg));
    }

}
