package org.huanmin.utils.common.media.img;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.huanmin.utils.common.base.UniversalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;

public class ImageUtil implements Serializable {
  private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
  private int width; // 图片宽
  private int height; // 图片高
  private Long size; // 图片大小 kb
  private String type; // 图片类型
  private String name; // 图片名称
  private static File file = null;

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "ImageUtiles{"
        + "width="
        + width
        + ", height="
        + height
        + ", size="
        + size
        + ", type='"
        + type
        + '\''
        + ", name='"
        + name
        + '\''
        + '}';
  }

  /**
   * 处理 单张 图片 信息
   *
   * @param imagepath 图片路径
   * @param pd 是否是网络 路径
   * @return void
   * @date 2015-7-25 下午7:30:47
   */
  public void readPic(String imagepath, boolean pd) throws Exception {
    File jpegFile;
    if (pd) {
      jpegFile = getFile(imagepath);
    } else {
      jpegFile = new File(imagepath);
    }
    Metadata metadata;
    try {
      metadata = ImageMetadataReader.readMetadata(jpegFile);
      for (Directory directory : metadata.getDirectories()) {
        for (Tag tag : directory.getTags()) {

          if (tag.getTagName().startsWith("Image Width")) {
            width = Integer.parseInt(tag.getDescription().split(" ")[0]);
          }
          if (tag.getTagName().startsWith("Image Height")) {
            height = Integer.parseInt(tag.getDescription().split(" ")[0]);
          }
          if (tag.getTagName().startsWith("Detected File Type Name")) {
            type = tag.getDescription();
          }
          if (tag.getTagName().startsWith("File Size")) {
            size = Long.parseLong(tag.getDescription().split(" ")[0]) / 1024;
          }
          if (tag.getTagName().startsWith("File Name")) {
            name = tag.getDescription();
          }
        }
      }

    } catch (ImageProcessingException | IOException e) {
       UniversalException.logError(e);
    }
    if (pd) {
      jpegFile.delete();
    }
  }

  /**
   * 网络图片 转为File对象
   *
   * @param url 图片url
   * @return File
   * @author dyc date: 2020/9/4 14:54
   */
  public static File getFile(String url) throws Exception {
    // 对本地文件命名
    String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
    File file = null;

    URL urlfile;
    InputStream inStream = null;
    OutputStream os = null;
    try {

      String s = System.getProperty("user.dir") + File.separator + fileName;
      file = new File(s);
      try {
        if (!file.exists()) {
          file.createNewFile();
        }
      } catch (Exception e) {
        System.out.println("新建文件操作出错");
         UniversalException.logError(e);
      }
      // 下载
      urlfile = new URL(url);
      inStream = urlfile.openStream();
      os = new FileOutputStream(file);

      int bytesRead = 0;
      byte[] buffer = new byte[8192];
      while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
        os.write(buffer, 0, bytesRead);
      }
    } catch (Exception e) {
       UniversalException.logError(e);
    } finally {
      try {
        if (null != os) {
          os.close();
        }
        if (null != inStream) {
          inStream.close();
        }

      } catch (Exception e) {
         UniversalException.logError(e);
      }
    }

    return file;
  }

  // 链接url下载图片
  public static void downloadPicture(String url1, String filepath) {
    URL url = null;
    String fileName = url1.substring(url1.lastIndexOf("/") + 1, url1.length());
    try {
      url = new URL(url1);
      DataInputStream dataInputStream = new DataInputStream(url.openStream());

      FileOutputStream fileOutputStream =
          new FileOutputStream(String.valueOf(Paths.get(filepath + fileName)));
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      byte[] buffer = new byte[1024];
      int length;

      while ((length = dataInputStream.read(buffer)) > 0) {
        output.write(buffer, 0, length);
      }
      byte[] context = output.toByteArray();
      fileOutputStream.write(context);
      dataInputStream.close();
      fileOutputStream.close();
    } catch (IOException e) {
       UniversalException.logError(e);
    }
  }

  // 判断网络图片是否存在
  public static boolean existRource(String source) {
    try {
      URL url = new URL(source);
      URLConnection uc = url.openConnection();
      InputStream in = uc.getInputStream();
      if (source.equalsIgnoreCase(uc.getURL().toString())) // 只要不异常那么就存在
      {
        return true;
      }
      in.close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
