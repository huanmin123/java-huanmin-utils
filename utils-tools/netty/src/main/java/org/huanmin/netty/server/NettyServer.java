package org.huanmin.netty.server;

import com.utils.common.base.SnowIdUtil;
import com.utils.common.base.UniversalException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.huanmin.netty.base.CustomHeartbeatHandler;
import org.huanmin.netty.base.HeartbeatHandlerEvent;
import org.huanmin.netty.base.MyDefaultChannelGroup;
import org.huanmin.netty.base.MyNioSocketChannel;
import org.huanmin.netty.server.channel.MyServerNewIdNioSocketChannel;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Getter
@Setter
public class NettyServer {
    private Integer port;
    private String key= SnowIdUtil.uniqueLongHex(); //服务端唯一标识
    // 创建ServerBootstrap实例，服务器启动对象
    private final ServerBootstrap bootstrap = new ServerBootstrap();

    // 创建boss线程组，用于接收连接
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    // 创建worker线程组，用于处理连接上的I/O操作，含有子线程NioEventGroup个数为CPU核数大小的2倍
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private List<ChannelHandler> channelHandlers;

    private Consumer<ChannelPipeline> consumer;

    //channel对象数组,这个是会话级别

    private  LogLevel logLevel=LogLevel.DEBUG; //日志级别
    private  Integer  so_backlog=1024; //TCP缓冲区
    //是否开启心跳处理 ,需要客户端和服务端都开启,否则会出现客户端发送心跳,服务端不回复
    private  boolean isCloseHeartbeatHandler=false;
    private int allHeartbeatTime=360;//心跳时间,单位秒, 如果180秒内没有向客户端发送任何数据或者没有接收到客户端的任何数据,则触发一个事件.
    private int writeHeartbeatTime=180;//心跳时间,单位秒, 如果180秒内没有向客户端发送任何数据,则触发一个事件.
    private int readHeartbeatTime=180;//心跳时间,单位秒, 如果180秒内没有接收到客户端的任何数据,则触发一个事件.
    private HeartbeatHandlerEvent heartbeatHandlerEvent;

    //会话通道,ChannelGroup自动会移除关闭的channel
    private  ChannelGroup clientChannelGroup;


    public NettyServer(Integer port, Consumer<ChannelPipeline> consumer) {
        this.port = port;
        this.consumer = consumer;
        this.clientChannelGroup=new MyDefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }
//    服务端的启动是阻塞的
    public void start() {
        try {
            // 使用链式编程配置参数
            // 将boss线程组和worker线程组暂存到ServerBootstrap
            bootstrap.group(bossGroup, workerGroup);

            // 设置服务端Channel类型为NioServerSocketChannel作为通道实现
            bootstrap.channel(MyServerNewIdNioSocketChannel.class);
            bootstrap.handler(new LoggingHandler(logLevel)); //设置日志级别
            // 添加ServerHandler到ChannelPipeline，对workerGroup的SocketChannel（客户端）设置处理器
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    clientChannelGroup.add(socketChannel);//添加到channelGroup
                    ChannelPipeline p = socketChannel.pipeline();
                    if (isCloseHeartbeatHandler){
                        p.addLast(new IdleStateHandler(readHeartbeatTime, writeHeartbeatTime,allHeartbeatTime ));
                        p.addLast(new CustomHeartbeatHandler(heartbeatHandlerEvent));//心跳处理
                    }
                    consumer.accept(p);
                }
            });

            // 设置启动参数，初始化服务器连接队列大小。服务端处理客户端连接请求是顺序处理，一个时间内只能处理一个客户端请求
            // 当有多个客户端同时来请求时，未处理的请求先放入队列中
            bootstrap.option(ChannelOption.SO_BACKLOG, so_backlog); //设置TCP缓冲区
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE); //检测连接是否有效,2小时探测一次 ,然后连续探测9次,如果9次都没有响应,则关闭连接
            log.info(key + ":服务端口绑定:{}", port);
            // 绑定端口并启动服务器，bind方法是异步的，sync方法是等待异步操作执行完成，返回ChannelFuture异步对象
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            log.info(key + ":服务启动成功，监听端口:{}", port);
            // 等待服务器关闭
            channelFuture.channel().closeFuture().sync();
            log.info(key + ":服务{}关闭:", port);
        } catch (InterruptedException e) {
            log.error(key + ":服务{}启动失败:{}",port, e.getMessage());
            UniversalException.logError(e);
        } finally {
            // 优雅地关闭boss线程组
            bossGroup.shutdownGracefully();
            // 优雅地关闭worker线程组
            workerGroup.shutdownGracefully();
        }
    }
    //关闭服务
    public void close() {
        log.info(key + ":服务端{}手动强制关闭自己", port);
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


    //给客户端发消息
    public void sendClientMessage(Object msg) {
        if (!clientChannelGroup.isEmpty()) {
            ChannelGroupFuture channelFutures = clientChannelGroup.writeAndFlush(msg);
            channelFutures.addListener(future -> {
                if (!future.isSuccess()) {
                    log.error("sendServerMessage-{}-服务端发送消息给客户端失败了,error:{},message:{}", key, future.cause().getMessage(), msg);
                }
            });
        }else{
            log.error("sendServerMessage-{}-服务端发送消息给客户端失败了,error:{},message:{}", key, "客户端连接不存在", msg);
        }
    }

    //给指定客户端发送消息

    /**
     *
     * @param clientId  唯一标识LongText
     * @param msg
     */
    public void sendClientMessage(String clientId,Object msg) {
        Channel channel = clientChannelGroup.find(MyNioSocketChannel.newChannelId(clientId));
        if (channel!=null&&channel.isActive()){
            ChannelFuture channelFuture = channel.writeAndFlush(msg);
            channelFuture.addListener(future -> {
                if (!future.isSuccess()) {
                    log.error("sendServerMessage-{}-服务端发送消息给客户端失败了,error:{},message:{}", key, future.cause().getMessage(), msg);
                }
            });
        }else{
            log.error("sendServerMessage-{}-服务端发送消息给客户端失败了,error:{},message:{}", key, "客户端连接不存在", msg);
        }
    }

}
