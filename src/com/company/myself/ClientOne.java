package com.company.myself;

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
 * date: 2020/5/6 11:57
 */
public class ClientOne {
    private static Selector selector;

    public static void main(String[] args) throws IOException {
        selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.bind(new InetSocketAddress(8080));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);


    }

    class ReadThread implements Runnable {

        private Selector selector;

        public ReadThread(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    channelSelect();

                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isReadable()) {
                            readMeg(selectionKey);
                        } else {
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            channel.register(selector, SelectionKey.OP_READ);
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void channelSelect() throws IOException, InterruptedException {
            while (selector.select() == 0) {
                Thread.sleep(500);
                System.out.println("等待......");
            }
        }

        private void readMeg(SelectionKey selectionKey) throws IOException {
            SocketChannel readChannel = (SocketChannel) selectionKey.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            readChannel.read(byteBuffer);

            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.print(byteBuffer.getChar());
            }
        }
    }
}
