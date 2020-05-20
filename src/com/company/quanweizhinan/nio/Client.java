package com.company.quanweizhinan.nio;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/4/21 16:41
 */
public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(8080));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                SocketChannel channel = (SocketChannel) selectionKey.channel();
                channel.configureBlocking(false);
                if (selectionKey.isReadable()) {
                    // 读取server返回的信息
                    ByteBuffer allocate = ByteBuffer.allocate(16);
                    channel.read(allocate);
                    allocate.flip();
                    byte[] bytes = allocate.array();
                    System.out.println(new String(bytes));
                    // 从控制台输入
                    allocate.clear();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    allocate.put(bufferedReader.readLine().getBytes());
                    allocate.flip();
                    channel.write(allocate);
                } else {
                    // 注册读写
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    channel.register(selector, SelectionKey.OP_READ);
                }
                iterator.remove();
            }
        }
    }
}
