package com.utils.common.file;

import org.apache.commons.net.ftp.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FtpUtil {

    private final String Control_Encoding = "UTF-8";
    private FTPClient client = null;
    private   String ip;
    private  int port;
    private  String userName;
    private  String passWord;
    private  String filepath;
    private  String localdir;
    public static final String DIRSPLIT = File.separator;
   
    public FtpUtil(String ip, int port, String userName, String passWord, String filepath, String localdir) {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.passWord = passWord;
        this.filepath = filepath;
        this.localdir = localdir;
    }
    
    /**
     * 获取当前FTP所在目录位置
     *
     * @return
     */
    public String getHome() {
        return this.filepath;
    }

    /**
     * 连接FTP Server
     *
     * @throws IOException
     */
    public static final String ANONYMOUS_USER = "anonymous";

    public void connect() throws Exception {
        if (client == null) {
            client = new FTPClient();
        }
        // 已经连接
        if (client.isConnected()) {
            return;
        }

        // 编码
        client.setControlEncoding(Control_Encoding);

        try {
            // 连接FTP Server
            client.connect(this.ip, this.port);
            // 登陆
            if (this.userName == null || "".equals(this.userName)) {
                // 使用匿名登陆
                client.login(ANONYMOUS_USER, ANONYMOUS_USER);
            } else {
                client.login(this.userName, this.passWord);
            }
            // 设置文件格式
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 获取FTP Server 应答
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                throw new Exception("connection FTP fail!");
            }

            FTPClientConfig config = new FTPClientConfig(client.getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            client.configure(config);
            // 使用被动模式设为默认
            client.enterLocalPassiveMode();
            // 二进制文件支持
            client.setFileType(FTP.BINARY_FILE_TYPE);
            // 设置模式
            client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);

        } catch (IOException e) {
            throw new Exception("connection FTP fail! " + e);
        }
    }

    /**
     * 断开FTP连接
     *
     * @throws IOException
     */
    public void close() {
        if (client != null && client.isConnected()) {
            try {
                client.logout();
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件列表
     *
     * @return
     */
    public List<FTPFile> list() {
        List<FTPFile> list = null;
        try {
            FTPFile ff[] = client.listFiles(filepath);
            if (ff != null && ff.length > 0) {
                list = new ArrayList<FTPFile>(ff.length);
                Collections.addAll(list, ff);
            } else {
                list = new ArrayList<FTPFile>(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 切换目录
     *
     * @param path 需要切换的目录
     */
    public void switchDirectory(String path) {
        try {
            if (path != null && !"".equals(path)) {
                boolean ok = client.changeWorkingDirectory(path);
                if (ok) {
                    this.filepath = path;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public void createDirectory(String path) {
        try {
            client.makeDirectory(filepath+DIRSPLIT+path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除目录，如果目录中存在文件或者文件夹则删除失败
     *
     * @param pathDirectory
     * @return
     */
    public boolean deleteDirectory(String pathDirectory) {
        boolean flag = false;
        try {
            flag = client.removeDirectory(filepath+DIRSPLIT+pathDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除基于ftp目录下的目录内所有文件,包括他自己
     *
     * @param path
     */

    public void delFtpAllFile(String path) {

        try {
            FTPFile[]  ff= client.listFiles(filepath+DIRSPLIT+path);
            for (FTPFile ftpFile : ff) {

                if (ftpFile.isFile()) {
                    //删除文件
                    deleteFile(path+DIRSPLIT+ftpFile.getName());
                }

                if (ftpFile.isDirectory()) {
                    delFtpAllFile(path+DIRSPLIT+ftpFile.getName());
                    //删除空目录
                    deleteDirectory(path+DIRSPLIT+ftpFile.getName());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //删除自身
        deleteDirectory(path);
    }



    /**
     * 删除指定文件
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName) {
        boolean flag = false;
        try {
            flag = client.deleteFile(filepath+DIRSPLIT+fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件，上传文件只会传到当前所在目录
     *
     * @param fileName
     *   本地文件
     * @return
     */
    public boolean upload(String fileName) {
        File localFile=new File(localdir+File.separator+fileName);
        return this.upload(localFile, filepath+DIRSPLIT+fileName);
    }

    /**
     * 上传文件，会覆盖FTP上原有文件
     *
     * @param localFile
     *            本地文件
     * @param reName
     *            重名
     * @return
     */
    public boolean upload(File localFile, String reName) {
        boolean flag = false;
        String targetName = reName;
        // 设置上传后文件名
        if (reName == null || "".equals(reName)) {
            targetName = localFile.getName();
        }
        FileInputStream fis = null;
        try {
            // 开始上传文件
            fis = new FileInputStream(localFile);
            client.setControlEncoding(Control_Encoding);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            boolean ok = client.storeFile(targetName, fis);
            if (ok) {
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 下载文件，如果存在会覆盖原文件
     *
     * @param fileName  FTP上的文件名称
     * @return
     */
    public boolean download(String fileName) {
        boolean flag = false;

        File dir = new File(localdir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileOutputStream fos = null;
        try {
            String saveFile = dir.getAbsolutePath() + File.separator + fileName;
            fos = new FileOutputStream(new File(saveFile));
            boolean ok = client.retrieveFile(filepath+DIRSPLIT+fileName, fos);
            if (ok) {
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }



}
