package org.huanmin.utils.netty.base;

import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @author huanmin
 * @date 2023/11/30
 */

@Data
public class MessageNetty {
    //消息类型
    private String type;

    //消息长度
    private int length;

    //消息体
    private ByteBuf msgBody;

    public MessageNetty(String type, ByteBuf msgBody) {
        this.type = type;
        this.msgBody = msgBody;
        this.length = msgBody.readableBytes();
    }
    public MessageNetty(String type, String msgBody) {
        this.type = type;
        this.msgBody = ByteBufUtil.createByteBuf(msgBody);
        this.length = this.msgBody.readableBytes();
    }

    public MessageNetty(String type, byte[] msgBody) {
        this.type = type;
        this.msgBody = ByteBufUtil.createByteBuf(msgBody);
        this.length = this.msgBody.readableBytes();
    }

    @Override
    public String toString() {
        return "MessageNetty{" +
                "type='" + type + '\'' +
                ", length=" + length +
                ", msgBody=" + ByteBufUtil.byteBufToStr(this.msgBody) +
                '}';
    }
}
