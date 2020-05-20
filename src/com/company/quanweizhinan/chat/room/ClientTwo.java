package com.company.quanweizhinan.chat.room;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/4/26 10:12
 */
public class ClientTwo implements Runnable {
    private Selector selector;
    private SocketChannel socketChannel;

    public ClientTwo(Selector selector,SocketChannel channel) {
        this.selector = selector;
        this.socketChannel = channel;
    }

    @Override
    public void run() {

        try {
            for (; ; ) {
                int readyChannels = selector.select();
                if (readyChannels == 0) continue;

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = (SelectionKey) iterator.next();

                    if (selectionKey.isReadable()) {
                        readHandler(selectionKey, selector);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readHandler(SelectionKey selectionKey, Selector selector)
            throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        System.out.println("是否channal相等：" + (socketChannel == this.socketChannel));
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        StringBuilder response = new StringBuilder();
        while (socketChannel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            response.append(StandardCharsets.UTF_8.decode(byteBuffer));
        }

        socketChannel.register(selector, SelectionKey.OP_READ);

        if (response.length() > 0) {
            System.out.println(response);
        }
    }

    public static void main(String[] args) throws IOException {
        String nickname = "zx";
        SocketChannel socketChannel = SocketChannel.open(
                new InetSocketAddress("127.0.0.1", 8000));

        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        //开启一个线程专门处理服务端发来的消息
        new Thread(new ClientTwo(selector, socketChannel)).start();

        //向服务端发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String request = scanner.nextLine();
            if (request != null && request.length() > 0) {
                socketChannel.write(StandardCharsets.UTF_8.encode(nickname + " : " + request));
            }
        }

    }
}
