package com.company.new_demo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/1/10 13:46
 */
public class SendDemo {
    public static void main(String[] args) throws IOException {
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
//        SocketChannel channel = SocketChannel.open();
//        channel.connect(new InetSocketAddress("localhost", 8090));
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        CharBuffer charBuffer = CharBuffer.allocate(1024);

        FileChannel fileChannel = new RandomAccessFile("C:\\Users\\liuchong\\Desktop\\channel.txt", "r").getChannel();
        int read = fileChannel.read(allocate);
        while (read != -1) {
            allocate.flip();
            decoder.decode(allocate, charBuffer, false);
            charBuffer.flip();
//            channel.write(allocate);
            while (charBuffer.hasRemaining()) {
//                System.out.print(allocate.getChar());
                System.out.print(charBuffer.get());
            }
            charBuffer.clear();
            allocate.clear();
            read = fileChannel.read(allocate);
        }
//        channel.close();
    }
}
