package com.company.new_demo;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/1/10 13:46
 */
public class SendDemo {

    private Selector selector;

    public SendDemo(Selector selector) {
        this.selector = selector;
    }

    private void init() throws IOException {
        selector = Selector.open();
    }


}
