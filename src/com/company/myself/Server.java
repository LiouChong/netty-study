package com.company.myself;


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
 * date: 2020/5/13 13:41
 */
public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        Selector selector = Selector.open();

        // 绑定8080
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select() == 0) {
                System.out.println("等等……");
                Thread.sleep(500);
                continue;
            }

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    // 有新连接则注册读事件
                    ServerSocketChannel serverSocket = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocket.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    socketChannel.write(StandardCharsets.UTF_8.encode("您和其他人不是好友，请注意安全！"));
                } else if (selectionKey.isReadable()) {
                    // 有读事件则广播给其他SocketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    String msg = revMsg(selectionKey);
                    broadcastToUser(selector, socketChannel, msg);
                }
                iterator.remove();
            }
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

    private static void broadcastToUser(Selector selector, SocketChannel sourceChannel, String msg) {
        Set<SelectionKey> keys = selector.keys();
        keys.forEach(selectionKey -> {
            SelectableChannel channel = selectionKey.channel();
            if (channel instanceof SocketChannel && !channel.equals(sourceChannel)) {
                try {
                    SocketChannel socketChannel = (SocketChannel) channel;
                    socketChannel.write(StandardCharsets.UTF_8.encode(msg));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
