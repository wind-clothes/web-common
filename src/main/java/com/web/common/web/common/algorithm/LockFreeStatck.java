package com.web.common.web.common.algorithm;

import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public class LockFreeStatck<E> {

  public void push(E value) {

  }

  public E pop() {
    return null;
  }

  static class Node<E> {
    volatile E item;
    volatile Node<E> next;

    Node(E item) {

    }
  }
}
