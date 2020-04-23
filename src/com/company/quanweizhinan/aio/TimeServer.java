package com.company.quanweizhinan.aio;

import java.io.IOException;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/4/20 16:14
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(8080);
        new Thread(asyncTimeServerHandler, "AIO-AsyncTimeServerHandler-001").start();
    }
}
