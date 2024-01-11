package com.huanmin.utils.server.controller.file;

import com.alibaba.fastjson.JSONObject;
import com.huanmin.utils.common.file.FileWebDownLoad;
import com.huanmin.utils.common.file.ResourceFileUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileUploadOrDownloadController {

    // 文件上传
    @RequestMapping("/uploads")
    public String httpUpload(@RequestParam("files") MultipartFile[] files) throws FileNotFoundException {

        String absolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("/uploads");
        System.out.println(absolutePath);
        JSONObject object=new JSONObject();
        for(int i=0;i<files.length;i++){
            String fileName = files[i].getOriginalFilename();  // 文件名
            File dest = new File(absolutePath +File.separator+ fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                files[i].transferTo(dest);
            } catch (Exception e) {
                e.printStackTrace();
                object.put("success",2);
                object.put("result","程序错误，请重新上传");
                return object.toString();
            }
        }
        object.put("success",1);
        object.put("result","文件上传成功");
        return object.toString();
    }
    // 文件上传
    @RequestMapping("/upload")
    public String httpUpload(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        String absolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("uploads");
        System.out.println(absolutePath);
        JSONObject object=new JSONObject();
        String fileName = file.getOriginalFilename();  // 文件名
        File dest = new File(absolutePath +File.separator+ fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
            object.put("success",2);
            object.put("result","程序错误，请重新上传");
            return object.toString();
        }
        object.put("success",1);
        object.put("result","文件上传成功");
        return object.toString();
    }



    //多文件上传
    @PostMapping("/uploads")
    public String uploadMore(@RequestParam("files") MultipartFile[] files ,@RequestParam("token") String token) throws IOException {
        System.out.println(token);
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String newName= UUID.randomUUID().toString()+fileName.substring(fileName.indexOf("."));
            String path="F:\\file\\up\\"+newName;
            System.out.println(path);
            File saveFile=new File(path);
            if(!saveFile.getParentFile().exists()){
                saveFile.getParentFile().mkdirs();
            }
            file.transferTo(saveFile);
        }
        return "success";
    }

    //文件下载
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName) throws IOException {
        FileWebDownLoad.download("F:\\file\\"+fileName);
    }

}
