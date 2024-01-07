package org.huanmin.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

/**
 * @author huanmin
 * @date 2023/12/1
 */
@FunctionalInterface
public interface HttpInterface{
    void handler(ChannelHandlerContext ctx,FullHttpRequest request, String contentType, HttpMethod method, String uri, HttpHeaders headers, ByteBuf content);
}
