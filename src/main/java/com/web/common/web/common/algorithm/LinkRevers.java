package com.web.common.web.common.algorithm;

/**
 * <pre>
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月27日 下午4:37:33
 */
public class LinkRevers {

  static class Node {
    private Node next;
    private Object value;

    public Node(String value) {
      this.value = value;
    }

    public Node getNext() {
      return next;
    }

    public void setNext(Node next) {
      this.next = next;
    }

    public Object getValue() {
      return value;
    }

    public void setValue(Object value) {
      this.value = value;
    }
  }

  public static Node revers(Node head) {
    Node reverNode = null;

    return reverNode;
  }

  /**
   * 利用迭代循环到链表最后一个元素，然后利用nextNode.setNext(head)把最后一个元素变为 第一个元素。
   * 
   * @param head
   */
  public static Node reverse(Node head) {
    if (null == head || null == head.getNext()) {
      return head;
    }
    // 取得最后一个节点。。。。
    Node reversedHead = reverse(head.getNext());
    // 将当前节点的下一节点的next指向自己本身
    head.next.next = head;

    head.next = null;
    return reversedHead;
  }

  public static void main(String[] args) {
    Node head = new Node("a");
    Node node1 = new Node("b");
    Node node2 = new Node("c");
    Node node3 = new Node("d");
    // 初始化链表
    head.setNext(node1);
    node1.setNext(node2);
    node2.setNext(node3);
    System.out.println("打印链表反转前：");

    Node reversedHead = reverse(head);
    // 设置head的下一个元素为null，注意：此时head已经成为链表尾部元素。
    System.out.println(reversedHead.value);
    Node tmpNode = reversedHead;
    while (tmpNode != null) {
      if (tmpNode != null) {
        System.out.println(tmpNode.value + "->");
        tmpNode = tmpNode.next;
      }
    }
  }
}
