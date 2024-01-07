package org.huanmin.netty.server.channel;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.SocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.huanmin.netty.base.MyNioSocketChannel;

import java.nio.channels.SocketChannel;
import java.util.List;
@Slf4j
public class MyServerNewIdNioSocketChannel extends NioServerSocketChannel {
    @Override
    protected int doReadMessages(List<Object> buf) throws Exception {
        SocketChannel ch = SocketUtils.accept(javaChannel());

        try {
            if (ch != null) {
                // 此处重写ID
                NioSocketChannel nioSocketChannel = new MyNioSocketChannel(this, ch);
                buf.add(nioSocketChannel);
                return 1;
            }
        } catch (Throwable t) {
            log.warn("Failed to create a new channel from an accepted socket.", t);

            try {
                ch.close();
            } catch (Throwable t2) {
                log.warn("Failed to close a socket.", t2);
            }
        }

        return 0;
    }
}
