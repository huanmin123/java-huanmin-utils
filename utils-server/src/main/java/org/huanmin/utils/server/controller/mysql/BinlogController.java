package org.huanmin.utils.server.controller.mysql;


import org.huanmin.utils.common.file.FileUtil;
import org.huanmin.utils.common.file.FileWebDownLoad;
import org.huanmin.utils.common.file.FileWebUpload;
import org.huanmin.utils.common.spring.HttpJsonResponse;
import org.huanmin.utils.file_tool.mysql_log.binlog.BinLogHandle;
import org.huanmin.utils.file_tool.mysql_log.conditionfilter.FilterProcessorSlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("binlog")
public class BinlogController {
    private static final Logger logger = LoggerFactory.getLogger(BinlogController.class);

    @Autowired
    private FilterProcessorSlow filterProcessorSlow;

    @Autowired
    private BinLogHandle binLogHandle;


    @PostMapping("uploads-binlog")
    public HttpJsonResponse<Void> fileuploadBinlog(HttpServletRequest request) throws IOException {
        //上传的位置   这句代码就是将文件上传到当前服务器的根目录下uploads文件夹里添加
        String path =ContextVariableMsql.binLogPath;
        FileWebUpload.fileUpload(path, request);
        //获取当前目录下全部文件
        return HttpJsonResponse.success();

    }

    //获取全部文件列表
    @GetMapping("getFiles-binlog")
    public HttpJsonResponse< List<String>>  getFilesSlowBinlog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path =ContextVariableMsql.binLogPath;
        List<String> filesAll = FileUtil.getFilesAll(path);
        List<String> fileNmaes=new ArrayList<>();
        filesAll.forEach((filePath)->{
            fileNmaes.add(new File(filePath).getName());
        });
        return  HttpJsonResponse.success(fileNmaes);
    }
    @GetMapping("delFiles-binlog/{fileName}")
    public HttpJsonResponse<Boolean> delFilesBinlog(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path =ContextVariableMsql.binLogPath;
        boolean delete = new File(path + File.separator + fileName).delete();
        return  HttpJsonResponse.success(delete);
    }



    @GetMapping("formatDownload/{fileName}")
    public void binlogFormat(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ContextVariableMsql.binLogPath+File.separator+fileName;
        String cleanFormat = binLogHandle.cleanFormat(path);
        FileWebDownLoad.download(cleanFormat);
        //删除文件
        new File(cleanFormat).delete();
    }

    @GetMapping("rollbackDownload/{fileName}")
    public void rollbackFormat(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ContextVariableMsql.binLogPath+File.separator+fileName;
        String recoveryStructures = binLogHandle.recoveryStructures(path);
        String rollback = binLogHandle.rollback(path);
        FileWebDownLoad.downloadZipFile(new ArrayList<File>(Arrays.asList(new File(recoveryStructures),new File(rollback))),"rollbackFile",response);
        //删除文件
        new File(recoveryStructures).delete();
        new File(rollback).delete();
    }
    @GetMapping("recoverAdd/{fileName}")
    public void recoverAdd(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ContextVariableMsql.binLogPath+File.separator+fileName;
        String recoveryIns = binLogHandle.recoveryIns(path);

        FileWebDownLoad.download(recoveryIns);
        //删除文件
        new File(recoveryIns).delete();
    }
    @GetMapping("recoverDel/{fileName}")
    public void recoverDel(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ContextVariableMsql.binLogPath+File.separator+fileName;
        String recoveryDel = binLogHandle.recoveryDel(path);

        FileWebDownLoad.download(recoveryDel);
        //删除文件
        new File(recoveryDel).delete();
    }

    @GetMapping("recoverUp/{fileName}")
    public  void recoveryUp(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ContextVariableMsql.binLogPath+File.separator+fileName;
        String recoveryUp = binLogHandle.recoveryUp(path);
        FileWebDownLoad.download(recoveryUp);
        //删除文件
        new File(recoveryUp).delete();
    }

    @GetMapping("recoverStructures/{fileName}")
    public void recoverStructures(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ContextVariableMsql.binLogPath+File.separator+fileName;
        String recoverStructures = binLogHandle.recoveryStructures(path);
        FileWebDownLoad.download(recoverStructures);
        //删除文件
        new File(recoverStructures).delete();
    }

    @GetMapping("recoverData/{fileName}")
    public void recoverData(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ContextVariableMsql.binLogPath+File.separator+fileName;
        String recoveryStructures = binLogHandle.recoveryStructures(path);
        String recoveryData = binLogHandle.recoveryData(path);
        FileWebDownLoad.downloadZipFile(new ArrayList<File>(Arrays.asList(new File(recoveryStructures),new File(recoveryData))),"recoverData",response);
        //删除文件
        new File(recoveryStructures).delete();
        new File(recoveryData).delete();
    }

}
