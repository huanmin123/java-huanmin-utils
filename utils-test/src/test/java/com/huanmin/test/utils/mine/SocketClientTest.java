package com.huanmin.test.utils.mine;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClientTest {
    @SneakyThrows
    public static void main(String[] args) {

            //创建和服务器连接
            Socket socket =new Socket("huitoushian.cn",20000);
            //将客户端的 消息 发给 服务端
            OutputStream os = socket.getOutputStream();
            os.write("你好服务器嘻嘻嘻嘻嘻嘻嘻嘻嘻寻寻寻寻寻寻".getBytes());
            //接受服务端返回来的消息
            InputStream is=socket.getInputStream();
            byte[] bytes=new byte[1024];
            int len=is.read(bytes);
            System.out.println(new String(bytes,0,len));
            //关闭 socket 通信
            socket.close();

    }
}
