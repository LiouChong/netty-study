package com.company.myself;


import jdk.internal.org.objectweb.asm.ByteVector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/5/13 13:41
 */
public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.bind(new InetSocketAddress(8080));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() == 0) {
            System.out.println("等等……");
            Thread.sleep(500);
        }

       Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();

            if (selectionKey.isAcceptable()){
                SocketChannel channel = (SocketChannel) selectionKey.channel();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
            } else if (selectionKey.isReadable()){
                String msg = revMsg(selectionKey);

            }

            iterator.remove();
        }
    }

    private static String revMsg(SelectionKey selectionKey) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer byt = ByteBuffer.allocate(1024);
        channel.read(byt);
        byt.flip();
        System.out.println("接收到" + stringBuilder.append(StandardCharsets.UTF_8.decode(byt)));
        return stringBuilder.toString();
    }

    private static void broadcastToUser(Selector selector, SocketChannel sourceChannel) {
        Set<SelectionKey> keys = selector.keys();
        keys.forEach(item -> {

        })  ;
    }
}
