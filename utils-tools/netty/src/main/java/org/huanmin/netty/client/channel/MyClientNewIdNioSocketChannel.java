package org.huanmin.netty.client.channel;

import io.netty.channel.Channel;
import org.huanmin.netty.base.MyNioSocketChannel;

import java.nio.channels.SocketChannel;

public class MyClientNewIdNioSocketChannel extends MyNioSocketChannel {
    public MyClientNewIdNioSocketChannel(Channel parent, SocketChannel socket) {
        super(parent, socket);
    }
    public MyClientNewIdNioSocketChannel() {
        super();
    }

}
