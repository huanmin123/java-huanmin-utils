package com.huanmin.test.utils_common.base;


import com.utils.common.media.img.ImageUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtilsTest.class);
    //网络 图片  的 详细信息
    @Test
    public void show() throws Exception {

        ImageUtil imageUtils=new ImageUtil();
        String path="https://pic.cnblogs.com/avatar/463242/20190215125753.png";
        imageUtils.readPic(path,true);
        System.out.println(imageUtils);

    }

    // 本地图片测试   的 详细信息
    @Test
    public void sho1w() throws Exception {
        ImageUtil imageUtils=new ImageUtil();
        String path="D:\\1.png";
        imageUtils.readPic(path,false);
        System.out.println(imageUtils);
    }

    // 下载网络图片到 本地
    @Test
    public void sh22ow() throws Exception {

        ImageUtil imageUtils=new ImageUtil();
        String path="https://pic.cnblogs.com/avatar/463242/20190215125753.png";
        imageUtils.downloadPicture(path,"D:\\");


    }

}
