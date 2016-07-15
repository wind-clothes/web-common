package com.web.common.web.common.algorithm;

import java.util.LinkedList;

public class DoubleEndQueue<Integer> {
    private LinkedList<Integer> list;

    public DoubleEndQueue() {
        list = new LinkedList<Integer>();
    }

    // No.1
    // 开始写代码，完成双端队列的方法
    public Integer pollFirst() {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Queue is empty");
        }
        Integer result = list.pollFirst();
        return result;
    }

    public boolean offerFirst(Integer node) {
        boolean result = list.offerFirst(node);
        return result;
    }

    public boolean offerLast(Integer node) {
        boolean result = list.offerLast(node);
        return result;
    }

    // end_code
    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {
        DoubleEndQueue doubleQueue = new DoubleEndQueue();
        System.out.println(doubleQueue.size());
        for (int i = 0; i < 5; i++) {
            doubleQueue.offerFirst(i);
        }
        for (int i = 0; i < 5; i++) {
            doubleQueue.offerLast(i);
        }
        System.out.println("size:" + doubleQueue.size());
        int size = doubleQueue.size();
        for (int i = 0; i < size; i++) {
            int remove = (int) doubleQueue.pollFirst();
            System.out.println("size:" + doubleQueue.size() + " remove:" + remove);
        }
        System.out.println(doubleQueue.size());

    }
}
