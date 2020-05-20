package com.company.quanweizhinan.nio_cor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/4/23 15:46
 */
public class Client {
    private Selector selector;

    public Client init(int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        selector = Selector.open();
        channel.connect(new InetSocketAddress(port));
        channel.register(selector, SelectionKey.OP_CONNECT);
        return this;
    }

    public void listen() throws IOException, InterruptedException {
        System.out.println("客户端启动");
        // 轮询访问selector
        while (true) {
            // 而select和poll则是遍历存放channel和事件的集合所以效率低下一些,还有其他的效率问题可以看看之前epoll和select的讲解
            selector.select();
            //获取注册在该复用器上的channel和channelEvent
            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                ite.remove();
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    channel.write(ByteBuffer.wrap(new String("send message to server.").getBytes()));
                    channel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");
                } else if (key.isReadable()) { // 判断该channel的channelEvent事件类型，也就是reactor模式中的分发器，如果把里面处理过程进行封装就是处理器了
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);
                    byte[] data = buffer.array();
                    String message = new String(data);
                    System.out.println("recevie message from server:, size:" + buffer.position() + " msg: " + message);
                    ByteBuffer outbuffer = ByteBuffer.wrap(("client.".concat(message)).getBytes());
                    channel.write(outbuffer);
                }
                Thread.sleep(1000);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Client().init(8080).listen();
    }
}
