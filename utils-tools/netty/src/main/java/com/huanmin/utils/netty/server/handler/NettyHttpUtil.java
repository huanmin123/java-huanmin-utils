package com.huanmin.utils.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.huanmin.utils.common.base.UniversalException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import com.huanmin.utils.netty.base.ByteBufUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author huanmin
 * @date 2023/12/1
 */
public class NettyHttpUtil {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONNECTION = "Connection";
    public static final String KEEP_ALIVE = "keep-alive";

    public static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String JSON_CONTENT_TYPE = "application/json";

    public static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";
    public static final String HTML_CONTENT_TYPE = "text/html";
    //文本类型
    public static final String TEXT_CONTENT_TYPE = "text/plain";

    public static final String JSON_CONTENT_TYPE_UTF_8 = "application/json;charset=utf-8";
    public static final String HTML_CONTENT_TYPE_UTF_8 = "text/html;charset=utf-8";
    /**
     * 响应HTTP的请求,文本返回值处理, 一旦调用就会关闭连接
     *
     * @param ctx
     * @param contentType 返回类型
     * @param str         返回值 ,   注意:如果是文本需要在结尾加上"\r\n"
     */
    public static void Response(ChannelHandlerContext ctx, String contentType, ByteBuf str, Map<String, Object> header) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, str);
        response.headers().set(CONTENT_TYPE, contentType);
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(CONNECTION, KEEP_ALIVE);
        if (header != null) {
            header.forEach((k, v) -> response.headers().set(k, v));
        }
        ctx.write(response);
        ctx.flush();
        ctx.close();
    }

    /**
     * 获取post请求的内容
     *
     * @param request
     * @return JSONObject或者Map<String, Object>
     */
    public static Object getRequestPostBody(FullHttpRequest request) {
        // 获取请求的 Content-Type 类型
        String contentType = request.headers().get(CONTENT_TYPE);
        // 获取报文信息
        ByteBuf jsonBuf = request.content();
        String jsonStr = ByteBufUtil.byteBufToStr(jsonBuf);

        switch (contentType) {
            case FORM_CONTENT_TYPE:
                JSONObject json = new JSONObject();
                // 最常见的 POST 提交数据的方式了。浏览器的原生 表单
                String[] keyvalues = jsonStr.split("&");
                for (int i = 0; i < keyvalues.length; i++) {
                    // 放入键值对
                    json.put(keyvalues[i], keyvalues[i + 1]);
                    // 指针前进一格
                    i++;
                }
                return json;

            case JSON_CONTENT_TYPE:
                // 告诉服务端消息主体是序列化后的 JSON 字符串
                return JSON.parseObject(jsonStr);
            case MULTIPART_CONTENT_TYPE:
                // 常见的 POST 数据提交的方式。我们使用表单上传文件时，必须让 表单的 enctype 等于 multipart/form-data。
                return getRequestFormData(request);
            case TEXT_CONTENT_TYPE:
                return jsonStr;
            default:
                throw new UniversalException("不支持的请求类型,目前还解析不了这种类型的请求:{}", contentType);
        }

    }

    /**
     * 获取url参数 , 例如：http://localhost:8080?name=netty&age=12
     *
     * @param request
     * @return
     */
    public static JSONObject getRequestUrlParams(FullHttpRequest request) {
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> parameters = decoder.parameters();
        JSONObject jsonObject = new JSONObject();
        Iterator<Map.Entry<String, List<String>>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> next = iterator.next();
            List<String> value = next.getValue();
            jsonObject.put(next.getKey(), value.size() > 1 ? value : next.getValue().get(0));
        }
        return jsonObject;
    }


    /**
     * 获取表单参数 , 就是http表单中的input标签的name和value
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getRequestFormData(FullHttpRequest request) {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
        Map<String, Object> params = new HashMap<>();
        for (InterfaceHttpData data : httpPostData) {
            //获取普通参数
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
            //获取文件
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {

                FileUpload fileUpload = (FileUpload) data;
                params.put(fileUpload.getName(), fileUpload);
            }

        }
        return params;
    }

    //只获取上传的所有文件
    public static List<FileUpload> getFormRequestFiles(FullHttpRequest request) throws IOException {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
        List<FileUpload> fileUploadList = new java.util.ArrayList<>();
        for (InterfaceHttpData data : httpPostData) {
            //获取文件
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {

                FileUpload fileUpload = (FileUpload) data;
                fileUploadList.add(fileUpload);
            }
        }
        return fileUploadList;
    }

    /**
     * 文件下载
     * @param file
     * @param channelHandlerContext
     */
    private void responseFileCopy(File file, ChannelHandlerContext channelHandlerContext) {

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().add(CONTENT_TYPE, "application/octet-stream");
        response.headers().add(CONTENT_LENGTH, file.length());

        Channel channel = channelHandlerContext.channel();

        FileInputStream ips = null;
        try {
            ips = new FileInputStream(file);
            byte[] by = new byte[1024];//每次读取1024个字节
            int read = -1;
            while ((read = ips.read(by, 0, by.length)) != -1) {
                response.content().writeBytes(by, 0, read);
            }
            channel.writeAndFlush(response);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ips != null) {
                try {
                    ips.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //返回HTMl

    /**
     *
     * @param ctx
     * @param html <h1>你好</h1>
     */
    public static void ResponseHtml(ChannelHandlerContext ctx, String html) {
        String msg = "<html> \t<meta charset=\"UTF-8\" />" + html + "</html>";
        ByteBuf byteBuf = ByteBufUtil.createByteBuf(msg);
        Response(ctx, HTML_CONTENT_TYPE_UTF_8, byteBuf, null);
    }

}
