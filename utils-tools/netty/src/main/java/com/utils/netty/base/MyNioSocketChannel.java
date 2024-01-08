package com.utils.netty.base;


import com.utils.common.base.SnowIdUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.channels.SocketChannel;

public class MyNioSocketChannel  extends NioSocketChannel {


    public MyNioSocketChannel() {
        super();
    }

    public MyNioSocketChannel(Channel parent, SocketChannel socket) {
        super(parent, socket);
    }

    @Override
    protected ChannelId newId() {
        return new ChannelId() {
            private String id = SnowIdUtil.uniqueLongHex();

            @Override
            public String asShortText() {
                return id;
            }

            @Override
            public String asLongText() {
                return id;
            }
            @Override
            public int compareTo(ChannelId o) {
                return id.equals(o.asLongText()) ? 1 : 0;
            }
        };
    }
    //创建一个新的channelId
    public static ChannelId newChannelId(String newId) {
        return new ChannelId() {

            @Override
            public String asShortText() {
                return newId;
            }

            @Override
            public String asLongText() {
                return newId;
            }
            @Override
            public int compareTo(ChannelId o) {
                return newId.equals(o.asLongText()) ? 1 : 0;
            }
        };
    }
}
