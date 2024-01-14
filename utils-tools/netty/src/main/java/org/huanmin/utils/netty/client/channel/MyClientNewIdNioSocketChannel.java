package org.huanmin.utils.netty.client.channel;

import org.huanmin.utils.netty.base.MyNioSocketChannel;
import io.netty.channel.Channel;

import java.nio.channels.SocketChannel;

public class MyClientNewIdNioSocketChannel extends MyNioSocketChannel {
    public MyClientNewIdNioSocketChannel(Channel parent, SocketChannel socket) {
        super(parent, socket);
    }
    public MyClientNewIdNioSocketChannel() {
        super();
    }

}
