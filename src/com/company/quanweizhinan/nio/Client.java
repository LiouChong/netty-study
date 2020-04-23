package com.company.quanweizhinan.nio;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
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
        socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);

        while (true) {
            if(selector.select() == 0) {
                System.out.println("等待中..");
                Thread.sleep(1000);
                continue;
            }
            System.out.println("有感兴趣的事件");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                SocketChannel channel = (SocketChannel) selectionKey.channel();

                if (selectionKey.isWritable()) {
                    ByteBuffer allocate = ByteBuffer.allocate(128);
                    allocate.put("你好啊".getBytes(StandardCharsets.UTF_8));
                    allocate.flip();
                    channel.write(allocate);
                } else if (selectionKey.isReadable()) {
                    ByteBuffer allocate = ByteBuffer.allocate(128);
                    channel.read(allocate);
                    byte[] bytes = new byte[allocate.limit()];
                    allocate.get(bytes);
                    System.out.println(Arrays.toString(bytes));
                }
                iterator.remove();
            }
        }
    }
}
