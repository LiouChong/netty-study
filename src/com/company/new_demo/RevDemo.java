package com.company.new_demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/1/10 9:51
 */
public class RevDemo {
    public static void main(String[] args) throws IOException {
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();

        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(8090));
        // 设置非阻塞，这样单个线程才可以切换channel
        channel.configureBlocking(false);
        ByteBuffer allocate = ByteBuffer.allocate(16);
        CharBuffer charBuffer = CharBuffer.allocate(16);
        while (true) {
            SocketChannel accept = channel.accept();
            if (accept != null) {
                System.out.println("打开一个socket");
                while (accept.read(allocate) != -1) {
                    allocate.flip();
                    decoder.decode(allocate, charBuffer, false);
                    charBuffer.flip();
                    System.out.print(charBuffer);

                    allocate.clear();
                    charBuffer.clear();
                }
            }
        }
    }
}


