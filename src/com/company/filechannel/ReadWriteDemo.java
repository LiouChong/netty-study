package com.company.filechannel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/12/3 15:58
 */
public class ReadWriteDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\liuchong\\Desktop\\11月工作.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        RandomAccessFile randomAccessFileWrite = new RandomAccessFile("C:\\Users\\liuchong\\Desktop\\channel.txt", "rw");
        FileChannel writeChannel = randomAccessFileWrite.getChannel();

        ByteBuffer readBuffer = ByteBuffer.allocate(16);

        while (true) {
            if (channel.position() == channel.size()) {
                System.out.println("读取完毕!");
                randomAccessFile.close();
                randomAccessFileWrite.close();
                return;
            }
            channel.read(readBuffer);
            System.out.println("read 16 Byte");
            readBuffer.flip();
            writeChannel.write(readBuffer);
            System.out.println("write 16 Byte");
            readBuffer.clear();
            System.out.println();
        }
    }
}
