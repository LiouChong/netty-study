package com.company.quanweizhinan.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/4/21 14:01
 */
public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if (selector.select() == 0) {
                System.out.print("等待");
                Thread.sleep(500);
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel channel = serverChannel.accept();
                    channel.configureBlocking(false);
                    ByteBuffer byt = ByteBuffer.allocate(16).put("Hello".getBytes());
                    byt.flip();
                    channel.write(byt);
                    // 注册读
                    channel.register(selector, SelectionKey.OP_READ);
                } else {
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 读之后原样将信息返回
                    ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                    channel.read(byteBuffer);
                    byte[] info = byteBuffer.array();
                    System.out.println("接收到" + new String(info));
                    byteBuffer.flip();
                    channel.write(byteBuffer);
                    System.out.println("已发送");
                }
            }
        }
    }
}
