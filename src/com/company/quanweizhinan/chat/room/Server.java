package com.company.quanweizhinan.chat.room;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/4/26 9:55
 */
public class Server {

    private static void acceptHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) (selectionKey.channel());
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        socketChannel.write(StandardCharsets.UTF_8.encode("您与聊天室内其他人都不是朋友关系，请注意隐私安全"));
        System.out.println("已发送提醒消息");
    }

    private static void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        StringBuilder request = new StringBuilder();
        while (socketChannel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            request.append(StandardCharsets.UTF_8.decode(byteBuffer));
        }

        // 将channel再次注册到selector上，监听他的可读事件
        // todo： 这里不注册会发生什么
//        socketChannel.register(selector, SelectionKey.OP_READ);

        if (request.length() > 0) {
            broadCast(selector, socketChannel, request.toString());
        }
    }

    private static void broadCast(Selector selector, SocketChannel sourceChannel, String request) {
        // 获取到所有已经接入的客户端channel
        // todo: selector.keys() 返回的是什么?所有注册的通道嘛？有用户退出会不会不返回了
        Set<SelectionKey> selectionKeySet = selector.keys();

        selectionKeySet.forEach(selectionKey -> {
            Channel targetChannel = selectionKey.channel();

            // 剔除发消息的客户端
            if (targetChannel instanceof SocketChannel && targetChannel != sourceChannel) {
                try {
                    // 将信息发送到targetChannel客户端
                    ((SocketChannel) targetChannel).write(StandardCharsets.UTF_8.encode(request));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8000));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动成功！");

            for (; ; ) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    Thread.sleep(500);
                    System.out.println("等一哈");
                    continue;
                }

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {

                    SelectionKey selectionKey = (SelectionKey) iterator.next();

                    if (selectionKey.isAcceptable()) {
                        System.out.println("接收到链接");
                        acceptHandler(selectionKey, selector);
                    }

                    if (selectionKey.isReadable()) {
                        readHandler(selectionKey, selector);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
