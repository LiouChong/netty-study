package com.company.myself;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/5/6 11:57
 */
public class ClientTwo {

    public static void main(String[] args) throws IOException {
        String name = "钟湘";

        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8080));
        socketChannel.configureBlocking(false);

        socketChannel.register(selector, SelectionKey.OP_READ);

        new Thread(new ReadThread(selector)).start();
        Scanner scanner = new Scanner(System.in);
        ByteBuffer allocate;
        while (scanner.hasNextLine()) {
            String inf = name + " : " + scanner.nextLine();
            allocate = ByteBuffer.allocate(1024);
            allocate.put(StandardCharsets.UTF_8.encode(inf));
            allocate.flip();
            socketChannel.write(allocate);
        }

    }

    static class ReadThread implements Runnable {

        private Selector selector;

        public ReadThread(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    while (selector.select() == 0) {
                        Thread.sleep(500);
                        System.out.println("客户端接收");
                    }

                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isReadable()) {
                            readMeg(selectionKey);
                        } else {
                            System.out.println("接收到事件并非read");
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void readMeg(SelectionKey selectionKey) throws IOException {
            SocketChannel readChannel = (SocketChannel) selectionKey.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            readChannel.read(byteBuffer);
            StringBuilder stringBuffer = new StringBuilder();
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                stringBuffer.append(StandardCharsets.UTF_8.decode(byteBuffer));
            }
            System.out.println(stringBuffer.toString());
        }
    }
}
