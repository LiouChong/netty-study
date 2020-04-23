package com.company.filechannel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/12 16:34
 */
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File("C:\\Users\\liuchong\\Desktop\\docker.txt"), "rw");
        FileChannel channel = randomAccessFile.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(4028);

        ByteBuffer[] buffers = {byteBuffer, byteBuffer2};
//        int byteRead = channel.read(byteBuffer);
        channel.read(buffers);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.out.print((char)byteBuffer.get());
        }
        byteBuffer2.flip();
        while (byteBuffer2.hasRemaining()) {

            System.out.print((char)byteBuffer2.get());
        }
        randomAccessFile.close();
    }
}
