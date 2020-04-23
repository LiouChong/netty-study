package com.company.deldemo;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/17 16:14
 */
public class SocketClientNio {
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    private Selector selector;
    private SocketChannel socketChannel;

    public SocketClientNio(Selector selector) throws IOException {
        this.selector = selector;
        socketChannel = SocketChannel.open();
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    public void writeInfo() throws IOException {
        writeBuffer.put("你好！".getBytes());
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        writeBuffer.clear();
    }

    public static void main(String[] args) {
//        SocketClientNio socketClientNio = new SocketClientNio();

    }
}
