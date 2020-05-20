package com.company.quanweizhinan.nio_cor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/4/23 15:51
 */
public class Server {
    //多路复用器
    private Selector selector;
    public Server init(int port) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        return this;
    }

    public void listen() throws IOException, InterruptedException {
        System.out.println("服务器端启动成功");
        // 使用轮询访问selector
        while (true) {
            selector.select();
            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                ite.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);
                    channel.write(ByteBuffer.wrap(new String("send message to client").getBytes()));
                    channel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端请求连接事件，打了招呼！！");
                } else if (key.isReadable()) {// 有可读数据事件
                    // 获取客户端传输数据可读取消息通道。
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 创建读取数据缓冲器
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = channel.read(buffer);
                    byte[] data = buffer.array();
                    String message = new String(data);
                    System.out.println("接收到" + message);
                    ByteBuffer outbuffer = ByteBuffer.wrap(message.getBytes());
                    channel.write(outbuffer);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Server().init(8080).listen();
    }

}
