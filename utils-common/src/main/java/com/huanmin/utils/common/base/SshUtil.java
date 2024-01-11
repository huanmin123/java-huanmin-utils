package com.huanmin.utils.common.base;


import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.scp.client.DefaultScpClientCreator;
import org.apache.sshd.scp.client.ScpClient;
import org.apache.sshd.scp.client.ScpClientCreator;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.impl.DefaultSftpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * ssh工具
 */
@Slf4j
public final class SshUtil {


    private String host;
    private String user;
    private String password;
    private int port;

    private ClientSession session;
    private SshClient client;

    /**
     * 创建一个连接
     *
     * @param host     地址
     * @param user     用户名
     * @param port     ssh端口
     * @param password 密码
     */
    public SshUtil(String host, int port, String user, String password) {
        this.host = host;
        this.user = user;
        this.port = port;
        this.password = password;
    }

    /**
     * 登录
     *
     * @return
     * @throws Exception
     */
    public boolean initialSession() {
        if (session == null) {

            try {
                // 创建 SSH客户端
                client = SshClient.setUpDefaultClient();
                // 启动 SSH客户端
                client.start();
                // 通过主机IP、端口和用户名，连接主机，获取Session
                session = client.connect(user, host, port).verify().getSession();
                // 给Session添加密码
                session.addPasswordIdentity(password);
                // 校验用户名和密码的有效性
                return session.auth().verify().isSuccess();

            } catch (Exception e) {
                log.info("Login Host:" + host + " Error", e);
                return false;
            }
        }

        return true;
    }

    /**
     * 关闭连接
     *
     * @throws Exception
     */
    public void close() throws Exception {
        //关闭session
        if (session != null && session.isOpen()) {
            session.close();
        }

        // 关闭 SSH客户端
        if (client != null && client.isOpen()) {
            client.stop();
            client.close();
        }
    }

    /**
     * 下载文件 基于sftp
     *
     * @param localPath  本地文件名，若为空或是*，表示目前下全部文件
     * @param remotePath 远程路径，若为空，表示当前路径，若服务器上无此目录，则会自动创建
     * @throws Exception
     */
    public boolean sftpGetFile(String localPath, String remotePath) {
        SftpClient sftp = null;
        InputStream is = null;
        try {
            if (this.initialSession()) {
                DefaultSftpClientFactory sftpFactory = new DefaultSftpClientFactory();
                sftp = sftpFactory.createSftpClient(session);
                is = sftp.read(remotePath);
                Path dst = Paths.get(localPath);
                Files.deleteIfExists(dst);
                Files.copy(is, dst);

            }
        } catch (Exception e) {
            log.error(host + "Local File " + localPath + " Sftp Get File:" + remotePath + " Error", e);
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (sftp != null) {
                    sftp.close();
                }

            } catch (Exception e) {
                log.error("Close Error", e);
            }

        }
        return true;
    }

    /**
     * 下载文件 基于sftp
     *
     * @param localPath  本地文件名，若为空或是*，表示目前下全部文件
     * @param remotePath 远程路径，若为空，表示当前路径，若服务器上无此目录，则会自动创建
     * @param outTime    超时时间 单位毫秒
     * @throws Exception
     */
    public boolean sftpGetFile(String localPath, String remotePath, int outTime) {
        ProcessWithoutTime process = new ProcessWithoutTime(localPath, remotePath, 1);
        int exitCode = process.waitForProcess(outTime);

        if (exitCode == -1) {
            log.error("{} put Local File {} To Sftp Path:{} Time Out", host, localPath, remotePath);
        }
        return exitCode == 0 ? true : false;
    }

    /**
     * 上传文件 基于sftp
     *
     * @param localPath  本地文件名，若为空或是*，表示目前下全部文件
     * @param remotePath 远程路径，若为空，表示当前路径，若服务器上无此目录，则会自动创建
     * @param outTime    超时时间 单位毫秒
     * @throws Exception
     */
    public boolean sftpPutFile(String localPath, String remotePath, int outTime) {
        ProcessWithoutTime process = new ProcessWithoutTime(localPath, remotePath, 0);
        int exitCode = process.waitForProcess(outTime);

        if (exitCode == -1) {
            log.error("{} put Local File {} To Sftp Path:{} Time Out", host, localPath, remotePath);
        }
        return exitCode == 0 ? true : false;
    }

    /**
     * @author wangzonghui
     * @date 2022年4月13日 下午2:15:17
     * @Description 任务执行线程，判断操作超时使用
     */
    class ProcessWithoutTime extends Thread {

        private String localPath;
        private String remotePath;
        private int type;
        private int exitCode = -1;

        /**
         * @param localPath  本地文件
         * @param remotePath sftp服务器文件
         * @param type       0 上传  1 下载
         */
        public ProcessWithoutTime(String localPath, String remotePath, int type) {
            this.localPath = localPath;
            this.remotePath = remotePath;
            this.type = type;
        }

        public int waitForProcess(int outtime) {
            this.start();

            try {
                this.join(outtime);
            } catch (InterruptedException e) {
                log.error("Wait Is Error", e);
            }

            return exitCode;
        }

        @Override
        public void run() {
            super.run();
            boolean state;
            if (type == 0) {
                state = sftpPutFile(localPath, remotePath);
            } else {
                state = sftpGetFile(localPath, remotePath);
            }

            exitCode = state == true ? 0 : 1;
        }
    }

    /**
     * 上传文件 基于sftp
     *
     * @param localPath  本地文件名，若为空或是*，表示目前下全部文件
     * @param remotePath 远程路径，若为空，表示当前路径，若服务器上无此目录，则会自动创建
     * @throws Exception
     */
    public boolean sftpPutFile(String localPath, String remotePath) {
        SftpClient sftp = null;
        OutputStream os = null;
        try {
            if (this.initialSession()) {
                DefaultSftpClientFactory sftpFactory = new DefaultSftpClientFactory();
                sftp = sftpFactory.createSftpClient(session);

                os = sftp.write(remotePath, 1024);
                Files.copy(Paths.get(localPath), os);
            }

        } catch (Exception e) {
            log.error(host + "Local File " + localPath + " Sftp Upload File:" + remotePath + " Error", e);
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (sftp != null) {
                    sftp.close();
                }

            } catch (Exception e) {
                log.error("Close Error", e);
            }
        }
        return true;
    }

    /**
     * 上传文件 基于scp
     *
     * @param localPath  本地文件名，若为空或是*，表示目前下全部文件
     * @param remotePath 远程路径，若为空，表示当前路径，若服务器上无此目录，则会自动创建
     * @throws Exception
     */
    public boolean scpPutFile(String localPath, String remotePath) {
        ScpClient scpClient = null;
        try {
            if (this.initialSession()) {
                ScpClientCreator creator = new DefaultScpClientCreator();

                // 创建 SCP 客户端
                scpClient = creator.createScpClient(session);

                // ScpClient.Option.Recursive：递归copy，可以将子文件夹和子文件遍历copy
                scpClient.upload(localPath, remotePath, ScpClient.Option.Recursive);

            } else {
                log.error("Host:{} User:{} Upload Local File:{} Error", host, user, localPath);
                return false;
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
            return false;
        } finally {
            // 释放 SCP客户端
            if (scpClient != null) {
                scpClient = null;
            }
        }

        return true;
    }

    /**
     * 下载文件 基于scp
     *
     * @param localPath  本地路径，若为空，表示当前路径 , 本地文件名，若为空或是*，表示目前下全部文件
     * @param remotePath 远程路径
     * @throws Exception
     */
    public boolean scpGetFile(String localPath, String remotePath) {
        ScpClient scpClient = null;
        try {
            if (this.initialSession()) {
                ScpClientCreator creator = new DefaultScpClientCreator();
                // 创建 SCP 客户端
                scpClient = creator.createScpClient(session);

                scpClient.download(remotePath, localPath);  //下载文件

            } else {
                log.error("Host:{} User:{} Get File:{} Error", host, user, remotePath);
                return false;
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
            return false;
        } finally {
            // 释放 SCP客户端
            if (scpClient != null) {
                scpClient = null;
            }

        }

        return true;
    }

    /**
     * 执行远程命令
     *
     * @param command 执行的命令
     * @return 0成功 1异常
     * @throws Exception
     */
    public int runCommand(String command) {
        ChannelExec channel = null;
        try {
            if (this.initialSession()) {
                channel = session.createExecChannel(command);
                int time = 0;
                boolean run = false;

                channel.open();
                ByteArrayOutputStream err = new ByteArrayOutputStream();
                channel.setErr(err);


                while (true) {
                    if (channel.isClosed() || run) {
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                    if (time > 1800) {
                        break;
                    }
                    time++;
                }

                int status = channel.getExitStatus();

                if (status > 0) {
                    log.info("{}  host:{} user:{} Run Is code:{} Message:{}", command, host, user, status, err.toString());
                }

                return status;
            } else {
                log.error("Host:{} User:{} Login Error", host, user);
                return 1;
            }

        } catch (Exception e) {
            log.error("Host " + host + " Run Command Error:" + command + " " + e.toString(), e);
            return 1;
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                    log.error("Close Connection Error");
                }
            }

        }
    }

    /**
     * 执行远程命令
     *
     * @param command 执行的命令
     * @return 0成功 其他 异常
     * @throws Exception
     */
    public SshModel run(String command) {
        SshModel sshModel = new SshModel();
        ChannelExec channel = null;
        try {
            if (this.initialSession()) {
                channel = session.createExecChannel(command);
                int time = 0;
                boolean run = false;

                channel.open();
                ByteArrayOutputStream info = new ByteArrayOutputStream();
                channel.setOut(info);
                ByteArrayOutputStream err = new ByteArrayOutputStream();
                channel.setErr(err);

                while (true) {
                    if (channel.isClosed() || run) {
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception ee) {
                    }
                    if (time > 180) {

                        break;
                    }
                    time++;
                }

                int status = channel.getExitStatus();

                sshModel.setCode(status);
                sshModel.setInfo(info.toString());
                sshModel.setError(err.toString());
            } else {
                log.error("Host:{} User:{} Login Error", host, user);
                sshModel.setCode(1);
                sshModel.setError("Loging Error");
            }

        } catch (Exception e) {
            log.error("Host " + host + "Run Command Error:" + command + " " + e.toString(), e);
            sshModel.setCode(1);

        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    log.error("Close Connection Error");
                }
            }
        }
        return sshModel;
    }

    public class SshModel {

        /**
         * 状态码
         */
        private int code;

        /**
         * 信息
         */
        private String info;

        /**
         * 错误信息
         */
        private String error;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return "SshModel [code=" + code + ", info=" + info + ", error=" + error + "]";
        }

    }

}
