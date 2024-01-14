package org.huanmin.utils.common.file;


import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *  Sftp 文件上传
 * @ClassName: FtpOperationUtil
 * @Description:sftp 工具类，使用的ECS上ftp需要sftp协议才能连接。
 * @date: 2020年4月6日 下午6:09:49
 * @Copyright:
 */
@Component
@Slf4j
public class SftpUtil implements Closeable , InitializingBean {


    private  String ip;
    private  int port;
    private  String userName;
    private  String passWord;
    private  String filepath;
    private  String localdir;
    
    /**
     * 超时数,一分钟
     */
    private final static int TIMEOUT = 60000;
    private final static int BYTE_LENGTH = 1024;


    public static final String DIRSPLIT = File.separator;
    
    public SftpUtil(String ip, int port, String userName, String passWord, String filepath, String localdir) {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.passWord = passWord;
        this.filepath = filepath;
        this.localdir = localdir;
    }
    
    private Session session;
    private ChannelSftp channelSftp;




//    当ben初始化后调用,这时候项目都加载完毕了
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            JSch jSch = new JSch();
            session = jSch.getSession(userName, ip, port);
            if (null != passWord) {
                session.setPassword(passWord);
            }
            session.setTimeout(TIMEOUT);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            session.setConfig(properties);
        } catch (Exception e) {
            log.error("init ip:{},port:{},userName:{},password:{} error:{}", ip,port, userName, passWord, e);
        }
    }



    /**
     * 获得服务器连接 注意：操作完成务必调用close方法回收资源
     *
     * @see SftpUtil#close()
     * @return
     */
    public boolean connection() {
        try {
            if (!isConnected()) {
                session.connect();

                channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();

                log.info("connected to host:{},userName:{}", session.getHost(), session.getUserName());
            }
            return true;
        } catch (JSchException e) {
            log.error("connection to sftp host:{} error:{}", session.getHost(), e);
            return false;
        }
    }

    /**
     * 从sftp服务器下载指定文件到本地指定目录
     * @return
     */
    public boolean get(String fileName) {
        if (isConnected()) {
            try {
                channelSftp.get(filepath+DIRSPLIT+fileName, localdir+ File.separator+fileName);
                return true;
            } catch (SftpException e) {
                log.error("get remoteFile:{},localPath:{}, error:{}", filepath, localdir, e);
            }
        }
        return false;
    }

    /**
     * 读取sftp上指定文件数据
     *
     * @param remoteFile
     * @return
     */
    public byte[] getFileByte(String remoteFile) {
        byte[] fileData;
        try (InputStream inputStream = channelSftp.get(filepath+DIRSPLIT+remoteFile)) {
            byte[] ss = new byte[BYTE_LENGTH];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int rc = 0;
            while ((rc = inputStream.read(ss, 0, BYTE_LENGTH)) > 0) {
                byteArrayOutputStream.write(ss, 0, rc);
            }
            fileData = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("getFileData remoteFile:{},error:{}", filepath+DIRSPLIT+remoteFile, e);
            fileData = null;
        }
        return fileData;
    }

    /**
     * 读取sftp上指定（文本）文件数据,并按行返回数据集合
     *
     * @param remoteFile
     * @param charsetName
     * @return
     */
    public List<String> getFileLines(String remoteFile, String charsetName) {
        List<String> fileData;
        try (InputStream inputStream = channelSftp.get(filepath+DIRSPLIT+remoteFile);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String str;
            fileData = new ArrayList<>();
            while ((str = bufferedReader.readLine()) != null) {
                fileData.add(str);
            }
        } catch (Exception e) {
            log.error("getFileData remoteFile:{},error:{}", filepath+DIRSPLIT+remoteFile, e);
            fileData = null;
        }
        return fileData;
    }

    /**
     * 上传本地文件到sftp服务器指定目录

     * @return
     */
    public boolean put(String fileName) {
        if (isConnected()) {
            try {
                channelSftp.put(localdir+File.separator+fileName,filepath+DIRSPLIT+fileName );
                return true;
            } catch (SftpException e) {
                log.error("put localPath:{}, remoteFile:{},error:{}", localdir+File.separator+fileName, filepath+DIRSPLIT+fileName, e);
                return false;
            }
        }
        return false;
    }

    /**
     * 从sftp服务器删除指定文件
     *
     * @param remoteFile
     * @return
     */
    public boolean delFile(String remoteFile) {
        if (isConnected()) {
            try {
                channelSftp.rm(filepath+DIRSPLIT+remoteFile);
                return true;
            } catch (SftpException e) {
                log.error("delFile remoteFile:{} , error:{}", filepath+DIRSPLIT+remoteFile, e);
            }
        }
        return false;
    }

    /**
     * 列出指定目录下文件列表
     *
     * @param remotePath
     * @return
     */
    public Vector ls(String remotePath) {
        Vector vector = null;
        if (isConnected()) {
            try {
                vector = channelSftp.ls(filepath+DIRSPLIT+remotePath);
            } catch (SftpException e) {
                vector = null;
                log.error("ls remotePath:{} , error:{}", remotePath, e);
            }
        }
        return vector;
    }

    /**
     * 列出指定目录下文件列表
     *
     * @param remotePath
     * @param filenamePattern
     * @return 排除./和../等目录和链接,并且排除文件名格式不符合的文件
     */
    public List<ChannelSftp.LsEntry> lsFiles(String remotePath, Pattern filenamePattern) {
        List<ChannelSftp.LsEntry> lsEntryList = null;
        if (isConnected()) {
            try {
                Vector<ChannelSftp.LsEntry> vector = channelSftp.ls(remotePath);
                if (vector != null) {
                    lsEntryList = vector.stream().filter(x -> {
                        boolean match = true;
                        if (filenamePattern != null) {
                            Matcher mtc = filenamePattern.matcher(x.getFilename());
                            match = mtc.find();
                        }
                        if (match && !x.getAttrs().isDir() && !x.getAttrs().isLink()) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                }
            } catch (SftpException e) {
                lsEntryList = null;
                log.error("lsFiles remotePath:{} , error:{}", remotePath, e);
            }
        }
        return lsEntryList;
    }

    /**
     * 判断链接是否还保持
     *
     * @return
     */
    public boolean isConnected() {
        if (session.isConnected() && channelSftp.isConnected()) {
            return true;
        }
        log.info("sftp server:{} is not connected", session.getHost());
        return false;
    }

    /**
     * 关闭连接资源
     */
    @Override
    public void close() throws IOException {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.quit();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        log.info("FTP session and channel is closed");
    }



}