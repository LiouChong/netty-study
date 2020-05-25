package com.company.netty.time.zhanbao.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * Author: Liuchong
 * Description: 会导致粘包的demo
 * date: 2020/5/23 14:21
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    public static final String QUERY = "Query Time Order";

    private int counter;

    // 每读取到一条消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8").substring(0, req.length - System.getProperty("line.separator").length());
        System.out.println("The time server receive order ：" + body + "; the counter is ：" + ++counter);

        String currentTime = QUERY.equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";

        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

        ctx.writeAndFlush(resp);
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
