package com.company.deldemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/17 16:05
 */
public class SocketServerNIO {
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private Selector selector;

    public SocketServerNIO() throws IOException {
        selector = Selector.open();
        // 创建一个chanel
        SocketChannel channel = SocketChannel.open();
        // channel绑定8090端口
        channel.bind(new InetSocketAddress(8090));
        // 将channel注册到selector当中
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void readInfo() throws IOException {
        while (true) {
            int select = selector.select();
            if (select == 0) {
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    int read = socketChannel.read(readBuffer);

                    while (read != -1) {
                        readBuffer.flip();

                        System.out.print((char)readBuffer.get());

                        readBuffer.clear();
                        read = socketChannel.read(readBuffer);
                    }
                }
            }
        }
    }

}
