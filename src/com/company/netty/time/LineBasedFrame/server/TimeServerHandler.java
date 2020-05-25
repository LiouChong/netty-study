package com.company.netty.time.LineBasedFrame.server;

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

    private int counter;

    // 每读取到一条消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("The time server receive order ：" + body + "；The counter is ：" + ++counter);
        String currentTime = QUERY.equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf buf = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(buf);
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
