package org.huanmin.utils.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ByteBufUtil {
    //body头部添加指定自己的头部 ,使用原来的byteBuf,会重置byteBuf的读写位置从0开始
    //添加的头部会根据len进行字节补齐
    public static void addHeadByteBuf(String head, int len, ByteBuf body) {

        byte[] bytes = head.getBytes(CharsetUtil.UTF_8);
        //长度不够，补齐
        if (bytes.length < len) {
            byte[] newBytes = new byte[len];
            System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            bytes = newBytes;
        }
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes(body);

        body.clear();//清空body
        //新的body写回去
        body.writeBytes(byteBuf);

    }
    //将ByteBuf按照len进行字节补齐, 使用原来的byteBuf,会重置byteBuf的读写位置从0开始
     public static void paddingBytes(int len, ByteBuf body) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(body);
        body.clear();//清空body
        //新的body写回去
        body.writeBytes(byteBuf);
        //补齐
        if (body.readableBytes() < len) {
            byte[] bytes = new byte[len - body.readableBytes()];
            body.writeBytes(bytes);
        }
    }

    //去掉头部,使用原来的byteBuf,不会改变byteBuf的读取位置
    //headLen去掉头部的长度, 对应createByteBuf的len
    public static ByteBuf removeHead(int headLen,ByteBuf byteBuf) {
        return byteBuf.slice(headLen, byteBuf.readableBytes() - headLen);
    }

    /**
     * 验证是否是自己的消息头部,使用原来的byteBuf,不会改变byteBuf的读取位置
     * @param headLen 头部长度
     * @param eqHead 期望的头部
     * @param byteBuf 字节缓冲区
     * @return 是否是自己的消息头部
     */
    public static boolean verify(int headLen,String eqHead,ByteBuf byteBuf) {
        ByteBuf slice = byteBuf.slice(0, headLen);
        String trim = slice.toString(CharsetUtil.UTF_8).trim();//取出来头部然后去掉空格
        if (!eqHead.equals(trim)) {
            return false;
        }
        return true;
    }

    //截取开头n个字节 ,,使用原来的byteBuf,不会改变byteBuf的读取位置
    public static String sliceToStr(ByteBuf byteBuf,int length ){
        ByteBuf slice = byteBuf.slice(0, length);
        return slice.toString(CharsetUtil.UTF_8);
    }
    //将字符串转换成byteBuf , 新的byteBuf
    public static ByteBuf createByteBuf(String msg) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(msg.getBytes(CharsetUtil.UTF_8));
        return byteBuf;
    }

    /**
     * 创建一个ByteBuf对象
     * @param msg 字节数组
     * @return 创建的ByteBuf对象
     */
    public static ByteBuf createByteBuf(byte[] msg) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(msg);
        return byteBuf;
    }


    //将byteBuf转换成字符串,使用原来的byteBuf,不会改变byteBuf的读取位置
    public static String byteBufToStr(ByteBuf byteBuf) {
        return byteBuf.toString(CharsetUtil.UTF_8);
    }
}
