package com.utils.common.file;



import com.utils.common.file.condense.ZipFileUtils;
import com.utils.common.spring.ContextAttribuesUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * web游览器下载指定服务器上的文件
 */
public class FileWebDownLoad {
    private static final Logger logger = LoggerFactory.getLogger(FileWebDownLoad.class);

    //文件下载给游览器
    public static void download(String filePath) {
        HttpServletResponse response = ContextAttribuesUtils.getResponse();
        File file = new File(filePath);
        try (
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                InputStream fis = new BufferedInputStream(new FileInputStream(file));
        ) {
            // 以流的形式下载文件。
            byte[] buffer = new byte[fis.available()];
            // 取得文件名。
            String filename = file.getName();
            fis.read(buffer);
            // 设置response的Header
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            toClient.write(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            //断点失败
            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
        }
    }

    /**
     * 将所有需要下载的文件以压缩包形式返回给游览器
     *
     * @param filePaths 批量下载的文件路径
     * @param response
     * @throws IOException
     */
    public static void downloadZipFile(List<File> filePaths, String zipName, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        ZipFileUtils.zip(zip, filePaths);
        byte[] data = outputStream.toByteArray();
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((zipName + ".zip").getBytes(), StandardCharsets.UTF_8));
        IOUtils.write(data, response.getOutputStream());
    }


    /**
     * 下载网络文件给游览器
     *
     * @param urlpath  下载地址
     * @param response
     * @throws MalformedURLException
     */
    public static void downloadNet(String urlpath, HttpServletResponse response) throws MalformedURLException {
        URL url = new URL(urlpath);
        try (
                ServletOutputStream outputStream = response.getOutputStream();
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                ByteArrayOutputStream output = new ByteArrayOutputStream();
        ) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context = output.toByteArray();
            String file = url.getFile();
            file = file.substring(file.lastIndexOf("/") + 1, file.length());
            // 设置response的Header
            String filenamedisplay = URLEncoder.encode(file, "UTF-8"); //将文件名进行转义 防止中文名称不显示导致文件错误
            response.addHeader("Content-Disposition", "attachment;filename=" + filenamedisplay);//展示给用户的

            outputStream.write(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载字符串到文件里给游览器
     *
     * @param str      字符串内容
     * @param fileName 文件内容
     * @param response
     */
    public static void downloadStr(String str, String fileName, HttpServletResponse response) {
        try (
                OutputStream os = response.getOutputStream();
        ) {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(), StandardCharsets.UTF_8));
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            // 将字节流传入到响应流里,响应到浏览器
            os.write(bytes);
            os.close();
        } catch (Exception ex) {
            throw new RuntimeException("导出失败");
        }
    }

}
