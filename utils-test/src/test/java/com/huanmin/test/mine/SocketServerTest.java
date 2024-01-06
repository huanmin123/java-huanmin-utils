package com.huanmin.test.mine;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerTest {

    @SneakyThrows
    public static void main(String[] args) {
        //指定访问 客服端的 端口
        ServerSocket server=new ServerSocket(10100);
        while (true){
            // 接收客户端发送的数据。如果未收到会一致阻塞
            Socket socket=server.accept();
            // 读取请求 来的信息
            InputStream is=socket.getInputStream();
            byte[] bytes=new byte[1024];
            int len=is.read(bytes);
            System.out.println(new String(bytes,0,len));

            // 服务器  返回 消息  给客户端
            OutputStream os= socket.getOutputStream();
            os.write("收到谢谢".getBytes());
            socket.close();
        }

    }

}
