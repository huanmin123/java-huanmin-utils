package org.huanmin.utils.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.huanmin.utils.netty.client.NettyClient;

/**
 * @author huanmin
 * @date 2023/11/24
 * 心跳处理器
 */
@Slf4j
public class  RepeatLikeClientHandler extends ChannelInboundHandlerAdapter {
    private NettyClient client;
    public RepeatLikeClientHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("###{}###服务端{}关闭了,启动重连",client.getKey(),ctx.channel().remoteAddress());
        client.doConnect();//触发重连
    }

}