package com.huanmin.utils.common.file;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.*;

/**
 * 图片处理
 */
public class ImageUtils {

    //通过判断图片的宽度和高度来确定是否是图片
    public static boolean isImage(String filePath) {
        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            return false;
        }
        Image img = null;
        try {
            img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }

    /**
     * 调整图片大小
     * Params:
     * src – 原图片地址
     * dest – 调整后的图片地址
     * width – 调整后的图片宽度
     * height – 调整后的图片高度
     * filledBlank – 是否补白
     * Throws:
     * Exception
     */
    public static void resizeImage(String src, String dest, int width, int height, boolean filledBlank)
            throws Exception {
        File srcFile = new File(src);
        File parent = new File(srcFile.getParent());
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!srcFile.isFile()) {
            throw new Exception(srcFile + "不是一个文件!");
        } else if (!isImage(src)) {
            throw new Exception(srcFile + "不是一个图片文件!");
        }

        double ratio = 0; // 缩放比例
        BufferedImage bi = ImageIO.read(srcFile);
        Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        if (filledBlank) {
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            if (width == itemp.getWidth(null)) {
                g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null),
                        Color.white, null);
            } else {
                g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null),
                        Color.white, null);
            }
        } else {
            g.drawImage(itemp, 0, 0, null);
        }
        g.dispose();

        ImageIO.write(image, "jpg", new File(dest));
    }

    /**
     * 裁剪图片
     * Params:
     * src – 原图片地址
     * dest – 生成的图片地址
     * cropWidth – 裁剪的宽度
     * cropHeight – 裁剪的高度
     * cropStartX – 裁剪位置的起始坐标x值
     * cropStartY – 裁剪位置的起始坐标y值
     * Exception
     */
    public static void cropImage(String src, String dest, int cropWidth, int cropHeight, int cropStartX, int cropStartY)
            throws Exception {
        File srcImgFile = new File(src);
        if (!srcImgFile.exists() && !srcImgFile.isFile()) {
            throw new Exception(srcImgFile + " 不是一个图片文件!");
        }
        BufferedImage img = ImageIO.read(srcImgFile);
        BufferedImage clipped = null;
        Dimension size = new Dimension(cropWidth, cropHeight);
        Rectangle clip = createClip(img, size, cropStartX, cropStartY);
        try {
            int w = clip.width;
            int h = clip.height;
            clipped = img.getSubimage(clip.x, clip.y, w, h);
        } catch (RasterFormatException rfe) {

        }
        if (clip != null) {
            File targetImgFile = new File(dest);
            File destParent = new File(targetImgFile.getParent());
            if (!destParent.exists()) {
                destParent.mkdirs();
            }
            ImageIO.write(clipped, "jpg", targetImgFile);
        }
    }

    /**
     *
     创建裁剪范围
     Params:
     img – 图片
     size – 图片大小
     clipX – 裁剪起始位置x坐标
     clipY – 裁剪起始位置y坐标
     Returns:
     Throws: Exception
     */
    private static Rectangle createClip(BufferedImage img, Dimension size, int clipX, int clipY) throws Exception {
        Rectangle clip = null;
        boolean isClipAreaAdjusted = false;
        if (clipX < 0) {
            clipX = 0;
            isClipAreaAdjusted = true;
        }
        if (clipY < 0) {
            clipY = 0;
            isClipAreaAdjusted = true;
        }

        if ((size.width + clipX) <= img.getWidth() && (size.height + clipY) <= img.getHeight()) {
            clip = new Rectangle(size);
            clip.x = clipX;
            clip.y = clipY;
        } else {
            if ((size.width + clipX) > img.getWidth()) {
                size.width = img.getWidth() - clipX;
            }
            if ((size.height + clipY) > img.getHeight()) {
                size.height = img.getHeight() - clipY;
            }
            clip = new Rectangle(size);
            clip.x = clipX;
            clip.y = clipY;
            isClipAreaAdjusted = true;
        }
        if (isClipAreaAdjusted) {
            throw new Exception("裁剪区域超过图片大小，请重新选择裁剪区域!");
        }
        return clip;
    }

    /**
     根据路径在页面展示附件图片
     Params:
     request –
     response –
     srcPath –
     */
    public static void viewImageByPath(HttpServletRequest request, HttpServletResponse response, String srcPath)
            throws IOException {
        OutputStream out = response.getOutputStream();
        InputStream in = new FileInputStream(srcPath);

        byte[] buffer = new byte[8192];
        while (true) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1) {
                break;
            }
            out.write(buffer, 0, bytesRead);
            out.flush();
        }
        in.close();
        out.close();
    }



}
