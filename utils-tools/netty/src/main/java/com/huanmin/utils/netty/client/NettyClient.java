package com.huanmin.utils.netty.client;


import com.huanmin.utils.common.base.SnowIdUtil;
import com.huanmin.utils.common.base.UniversalException;
import com.huanmin.utils.netty.base.*;
import com.huanmin.utils.netty.client.channel.MyClientNewIdNioSocketChannel;
import com.huanmin.utils.netty.client.handler.RepeatLikeClientHandler;
import com.utils.netty.base.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@Getter
@Setter
public class NettyClient {


    private Integer port;
    private String ip;
    private String key= SnowIdUtil.uniqueLongHex(); //客户端唯一标识
    private final NioEventLoopGroup groupThread = new NioEventLoopGroup();
    private Channel channel;
    private  boolean isClose=false;
    private Bootstrap bootstrap = new Bootstrap();

    private List<ChannelHandler> channelHandlers;
    private  Consumer<ChannelPipeline> consumer;
    //是否开启心跳处理 ,需要客户端和服务端都开启,否则会出现客户端发送心跳,服务端不回复
    private  boolean isCloseHeartbeatHandler=false;
    //是否开启断线重连
    private  boolean isCloseRepeatLikeHandler=false;


    private int allHeartbeatTime=180;//心跳时间,单位秒, 如果180秒内没有向服务器发送任何数据或者没有从服务器接收到任何数据,则触发一个事件.
    private int writeHeartbeatTime=60;//心跳时间,单位秒, 如果180秒内没有向服务器发送任何数据,则触发一个事件.
    private int readHeartbeatTime=60;//心跳时间,单位秒, 如果180秒内没有从服务器接收到任何数据,则触发一个事件.
    private HeartbeatHandlerEvent heartbeatHandlerEvent; //内置的有HeartbeatLogHandlerDefaultEvent日志打印,可以自定义实现事件监听来处理心跳

    //会话通道,ChannelGroup自动会移除关闭的channel
    private  ChannelGroup serverChannelGroup;

    public NettyClient(String ip, Integer port,  Consumer<ChannelPipeline> consumer) {
        this.ip = ip;
        this.port = port;
        this.consumer = consumer;
         this.serverChannelGroup=new MyDefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    }
    //客户端的启动是非阻塞的
    public void start() {
        try {
            bootstrap.group(groupThread)
                    .channel(MyClientNewIdNioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            serverChannelGroup.add(socketChannel);//添加到channelGroup
                            ChannelPipeline p = socketChannel.pipeline();
                            if (isCloseRepeatLikeHandler){
                                p.addLast(new RepeatLikeClientHandler(NettyClient.this));//断线重连处理
                            }else{
                                p.addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                        //关闭客户端,关闭线程组
                                        log.info("###################客户端和服务端断开连接########################:{}", ctx.channel());
                                        groupThread.shutdownGracefully();
                                    }
                                });//日志打印
                            }
                            if(isCloseHeartbeatHandler){
                                p.addLast(new IdleStateHandler(readHeartbeatTime, writeHeartbeatTime, allHeartbeatTime));
                                p.addLast(new CustomHeartbeatHandler(heartbeatHandlerEvent));//心跳处理
                            }
                            //客户端处理
                            consumer.accept(p);

                        }
                    });
            doConnect();//连接
            if (isCloseHeartbeatHandler){
                sendData();//发送心跳
            }

        } catch (Exception e) {
            log.error(key + ":客户端启动失败:{}", e.getMessage());
            UniversalException.logError(e);
        }
    }


    public void doConnect() {
        if (isClose){
            log.info(key + ":客户端已经强制手动关闭了,不再重连");
            return;
        }
        if (channel != null && channel.isActive()) {
            return;
        }
        log.info(key + ":客户端启动成功，监听: {}:{}", ip, port);
        ChannelFuture future = bootstrap.connect(ip, port);
        future.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                channel = futureListener.channel();
                log.info(key + ":连接{}:{}服务器成功", ip, port);
            } else {
                if (isCloseRepeatLikeHandler){
                    log.error(key + ":连接服务器{}失败，稍后再试 10s", ip + ":" + port);
                    futureListener.channel().eventLoop().schedule(this::doConnect, 10, TimeUnit.SECONDS);
                }else {
                    log.error(key + ":连接服务器{}失败", ip + ":" + port);
                }
            }
        });
    }

    //强制关闭客户端
    public void close() {
        log.info(key + ":客户端{}:{}手动强制关闭自己", ip, port);
        isClose=true;
        groupThread.shutdownGracefully();
    }


    private void sendData() {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; true; i++) {
            if (channel != null && channel.isActive()) {
                String content = key + ":client msg " + i;
                channel.writeAndFlush(ByteBufUtil.createByteBuf(CustomHeartbeatHandler.CUSTOM_MSG+content));
            }
            try {
                Thread.sleep(random.nextInt(10000));//每隔10秒发送一次心跳,判断是否断线
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    //给服务端发消息
    public void sendServerMessage(Object msg){
        if (!serverChannelGroup.isEmpty()){
            ChannelGroupFuture channelFutures = serverChannelGroup.writeAndFlush(msg);
            channelFutures.addListener(future -> {
                if (!future.isSuccess()) {
                    log.error("sendServerMessage-{}-客服端发送消息给服务端失败了,error:{},message:{}", key, future.cause().getMessage(), msg);
                }
            });
        }else{
            log.error("sendServerMessage-{}-客服端发送消息给服务端失败了,error:{},message:{}", key, "服务端连接不存在", msg);
        }

    }

    //给指定服务端发送消息

    /**
     *
     * @param serverId 唯一标识LongText
     * @param msg
     */
    public void sendServerMessage(String serverId,Object msg){
        Channel channel = serverChannelGroup.find(MyNioSocketChannel.newChannelId(serverId));
        if (channel!=null&&channel.isActive()){
            ChannelFuture channelFuture = channel.writeAndFlush(msg);
            channelFuture.addListener(future -> {
                if (!future.isSuccess()) {
                    log.error("sendServerMessage-{}-客服端发送消息给服务端失败了,error:{},message:{}", key, future.cause().getMessage(), msg);
                }
            });
        }else{
            log.error("sendServerMessage-{}-客服端发送消息给服务端失败了,error:{},message:{}", key, "服务端连接不存在", msg);
        }
    }

}
