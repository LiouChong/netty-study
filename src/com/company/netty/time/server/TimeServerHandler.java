package com.company.netty.time.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/5/23 14:21
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    public static final String QUERY = "Query Time Order";
    // 每读取到一条消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // msg 转换为 ByteBuf，类似ButeBuffer对象，不过提供了更强大和灵活的功能
        ByteBuf buf = (ByteBuf) msg;
        // 根据可读取的字节数，创建字节数组
        byte[] req = new byte[buf.readableBytes()];
        // 将数据读取到数组中
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("The time server receive order : " + body);

        String curTime = QUERY.equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";;
        ByteBuf resp = Unpooled.copiedBuffer(curTime.getBytes());
        ctx.write(resp);
    }

    // 读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // netty并不会直接将数据发送到SocketChannel中，而是要先放到缓存区。因此这里将数据直接发送到SocketChannel。
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
