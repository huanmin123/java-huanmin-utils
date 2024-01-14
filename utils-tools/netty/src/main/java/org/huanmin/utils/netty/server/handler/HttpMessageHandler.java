package org.huanmin.utils.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huanmin
 * @date 2023/12/1
 */

@Slf4j
public class HttpMessageHandler extends ChannelInboundHandlerAdapter {
    private  HttpInterface httpInterface;
    public HttpMessageHandler(HttpInterface httpInterface){
        this.httpInterface=httpInterface;
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //只处理HTTP请求
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            // 获取请求方式
            HttpMethod method = req.method();
            // 判断请求方式是GET还是POST

            String uri = req.uri();
            if ("/favicon.ico".equals(uri)) {
                System.out.println("favicon.ico图标不响应");
                return;
            }
            log.info("浏览器请求路径:{} {}",method.name(), uri);
            ctx.channel().eventLoop().execute(()->{
                httpInterface.handler(ctx,req, req.headers().get(HttpHeaderNames.CONTENT_TYPE), method, uri, req.headers(), req.content());
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("HttpMessageHandler exceptionCaught:{}", cause.getMessage());
        ctx.close();
    }

}
