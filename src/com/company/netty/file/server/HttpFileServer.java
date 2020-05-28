package com.company.netty.file.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/5/28 0028 下午 8:52
 */
public class HttpFileServer {
    private static final String DEFAULT_URL = "/src/com/company";

    public void run(final int port, final String url) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 添加http请求消息解码器
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            // httpObjectAggregator解码器，作用是将多个消息转换为单一的FullHttpRequest或者FillHttpResponse，
                            // 原因是Http解码器在每个HTTP消息中会生成多个消息对象
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            // HTTP响应编码器，对HTTP响应消息进行编码
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });

            ChannelFuture f = b.bind("localhost", port).sync();
            System.out.println("HTTP文件目录服务器启动，网址是：" + "http://192.168.1.102：" + port + url);
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String url = DEFAULT_URL;

        new HttpFileServer().run(port, url);
    }

}
