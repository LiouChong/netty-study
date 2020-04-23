package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/1/10 13:22
 */
public class Exchange {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>(5000);
        for (int i = 0; i < 5; i++) {
            arrayList.add(i, i);
        }
        long l = System.currentTimeMillis();
        ListIterator<Integer> integerListIterator = arrayList.listIterator();
        while (integerListIterator.hasNext()) {
            Integer next = integerListIterator.next();
            if (next.equals(2)) {
                integerListIterator.remove();
                break;
            }
        }
        arrayList.add(0,2);
        long l1 = System.currentTimeMillis();
        System.out.println(l1 - l);
        System.out.println(arrayList);
    }
}
