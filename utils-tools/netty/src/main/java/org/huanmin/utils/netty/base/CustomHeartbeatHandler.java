package org.huanmin.utils.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @author huanmin
 * @date 2023/11/24
 */
@Slf4j
public class CustomHeartbeatHandler extends ChannelInboundHandlerAdapter {
    public static final String PING_MSG = "#PING&MSG#"; //客户端发送的消息
    public static final String PONG_MSG = "#PONG&MSG#"; //服务端回复的消息
    public static final String CUSTOM_MSG = "#CUSTOM&MSG#"; //最长为12个字节  ,客户端发送的消息

    private int heartbeatCount = 0;
    private HeartbeatHandlerEvent heartbeatHandler = new HeartbeatLogHandlerDefaultEvent();

    public CustomHeartbeatHandler( HeartbeatHandlerEvent heartbeatHandlers) {
        if (heartbeatHandlers != null) {
            //向下转型
            this.heartbeatHandler = heartbeatHandlers;
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (ByteBufUtil.sliceToStr((ByteBuf) byteBuf, PING_MSG.length()).equals(PING_MSG)) { //客户端发送来ping消息,回复pong消息
            context.channel().writeAndFlush(ByteBufUtil.createByteBuf(PONG_MSG));
            heartbeatCount++;
            log.info("接收到{}的ping, 我们回应pong ,健康检测次数:{} ", context.channel().remoteAddress(), heartbeatCount);
            ReferenceCountUtil.release(byteBuf);
        } else if (ByteBufUtil.sliceToStr(byteBuf, PONG_MSG.length()).equals(PONG_MSG)) { //服务端回复的消息
            log.info("接收到{}的回应 ", context.channel().remoteAddress());
            ReferenceCountUtil.release(byteBuf);
        } else if (ByteBufUtil.sliceToStr(byteBuf, CUSTOM_MSG.length()).equals(CUSTOM_MSG)) { //客户端发送的消息
            byteBuf.skipBytes(CUSTOM_MSG.length());
            String msg1 = ByteBufUtil.byteBufToStr(byteBuf);
            log.info("接收到{}发来消息:{}", context.channel().remoteAddress(), msg1);
            ReferenceCountUtil.release(byteBuf);
        } else {
            super.channelRead(context, msg);
        }
    }

    protected void sendPingMsg(String type, ChannelHandlerContext context) {
        context.channel().writeAndFlush(ByteBufUtil.createByteBuf(PING_MSG));
        heartbeatCount++;
        log.info("###{}###{} 发送 ping 消息给{} ", type, context.channel().localAddress(), context.channel().remoteAddress());
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    super.userEventTriggered(ctx, evt);
            }
        }
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        sendPingMsg("READER_IDLE", ctx);//发送心跳消息
        heartbeatHandler.handleReaderIdle(ctx);
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        sendPingMsg("WRITER_IDLE", ctx);//发送心跳消息
        heartbeatHandler.handleWriterIdle(ctx);
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        sendPingMsg("ALL_IDLE", ctx);//发送心跳消息
        heartbeatHandler.handleAllIdle(ctx);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("###{}###:异常:{}", ctx.channel().remoteAddress(), cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }


}
