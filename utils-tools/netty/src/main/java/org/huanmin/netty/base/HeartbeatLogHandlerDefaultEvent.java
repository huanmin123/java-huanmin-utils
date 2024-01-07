package org.huanmin.netty.base;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳事件默认实现
 */
@Slf4j
public class HeartbeatLogHandlerDefaultEvent implements HeartbeatHandlerEvent {

    @Override
    public void handleReaderIdle(ChannelHandlerContext ctx) {
        //读超时
        log.error("###{}###: 读空闲",ctx.channel().remoteAddress());
    }

    @Override
    public void handleWriterIdle(ChannelHandlerContext ctx) {
        //写超时
        log.error("###{}###: 写空闲",ctx.channel().remoteAddress());
    }

    @Override
    public void handleAllIdle(ChannelHandlerContext ctx) {
        //总超时
        log.error("###{}###: 读写都空闲",ctx.channel().remoteAddress());
    }
}
