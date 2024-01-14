package org.huanmin.utils.netty.base;

import io.netty.channel.ChannelHandlerContext;

/*
    * 心跳事件接口
 */
public interface HeartbeatHandlerEvent {


    void handleReaderIdle(ChannelHandlerContext ctx);

    void handleWriterIdle(ChannelHandlerContext ctx);

     void handleAllIdle(ChannelHandlerContext ctx);
}
