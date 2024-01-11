package com.huanmin.utils.server.controller.file;


import com.huanmin.utils.common.base.UniversalException;
import com.huanmin.utils.common.container.ArrayByteUtil;
import com.huanmin.utils.common.file.RandomAccessFileUtil;
import com.huanmin.utils.common.file.ResourceFileUtil;
import com.huanmin.utils.common.spring.HttpJsonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/fileslice")
public class FIleSliceDownloadController {
    private final String uploaddir = "uploads" + File.separator + "real" + File.separator;//实际文件目录

    // 获取文件的大小
    @GetMapping("/fIleSliceDownloadSize/{fileName}")
    public HttpJsonResponse<Long> getFIleSliceDownloadSize(@PathVariable String fileName) {
        String absoluteFilePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath(uploaddir) + File.separator + fileName;
        File file = new File(absoluteFilePath);
        if (file.exists() && file.isFile()) {
            return HttpJsonResponse.success(file.length());
        }

        return HttpJsonResponse.failed();
    }

    /**
     * 分段下载文件
     *
     * @param fileName 文件名称
     * @param begin    从文件什么位置开始读取
     * @param end      到什么位置结束
     * @param last     是否是最后一次读取
     * @param response
     */
    @GetMapping("/dwnloadsFIleSlice/{fileName}/{begin}/{end}/{last}")
    public void dwnloadsFIleSlice(@PathVariable String fileName, @PathVariable long begin, @PathVariable long end, @PathVariable boolean last, HttpServletResponse response) {
        String absoluteFilePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath(uploaddir) + File.separator + fileName;
        File file = new File(absoluteFilePath);
        try (OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            long readSize = end - begin;
            //读取文件的指定字节
            byte[] bytes = new byte[(int) readSize];
            RandomAccessFileUtil.randomAccessFileReadBytes(file, (int) begin, bytes);
            if (readSize <= file.length() || last) {
                bytes = ArrayByteUtil.getActualBytes(bytes); //去掉多余的
            }

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Length", String.valueOf(bytes.length));
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            toClient.write(bytes);

        } catch (Exception e) {
            UniversalException.logError(e);
        }
    }

}
