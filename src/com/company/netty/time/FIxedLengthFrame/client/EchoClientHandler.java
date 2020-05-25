package com.company.netty.time.FIxedLengthFrame.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/5/24 0024 下午 4:31
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    private int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String s = "This is " + ++count + " times, send!";
        s += "$_";
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(s.getBytes()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
