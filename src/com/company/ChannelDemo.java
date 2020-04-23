package com.company;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ChannelDemo {

    public static void main(String[] args) throws IOException {
        channelTest();
    }

    public static void channelTest() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();

        RandomAccessFile rw = new RandomAccessFile("C:\\Users\\liuchong\\Desktop\\channel.txt", "rw");
        FileChannel fileChannel = rw.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        CharBuffer charBuffer = CharBuffer.allocate(64);

        int bytesRead = fileChannel.read(byteBuffer);
        while (bytesRead != -1) {
            byteBuffer.flip();
            decoder.decode(byteBuffer, charBuffer, false);
            charBuffer.flip();
            System.out.print(charBuffer);
            byteBuffer.clear();
            charBuffer.clear();
            bytesRead = fileChannel.read(byteBuffer);
        }
        rw.close();
    }

    public static void GatheringTest() throws IOException {
        Charset charset = Charset.forName("GBK");
        CharsetDecoder decoder = charset.newDecoder();

        RandomAccessFile rw = new RandomAccessFile("C:\\Users\\Antiy.000\\Desktop\\docker.txt", "rw");
        FileChannel fromChannel = rw.getChannel();

        RandomAccessFile rw2 = new RandomAccessFile("C:\\Users\\Antiy.000\\Desktop\\channel.txt", "rw");
        FileChannel toChannel = rw2.getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(fromChannel, 0, count);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        toChannel.read(byteBuffer);

        CharBuffer charBuffer = CharBuffer.allocate(1024);

        byteBuffer.flip();
        toChannel.write(byteBuffer);
        decoder.decode(byteBuffer, charBuffer, false);
        charBuffer.flip();
        System.out.println(charBuffer);
    }

    public static void selectorDemo() throws IOException {
        File file = new File("C:\\Users\\Antiy.000\\Desktop\\channelWrite.txt");
        if (file.exists()) {
            file.mkdir();
        }
        RandomAccessFile rw = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = rw.getChannel();
        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
        String writeWord = "test test test test test test test test test";
        writeBuffer.put(writeWord.getBytes());
        writeBuffer.flip();
        fileChannel.write(writeBuffer);
        writeBuffer.clear();
    }
}
