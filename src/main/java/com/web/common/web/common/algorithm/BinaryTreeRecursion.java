package com.web.common.web.common.algorithm;

import java.util.LinkedList;
import java.util.Stack;

/**
 * <pre>
 * 递归
 * </pre>
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2016年5月21日 上午12:22:11
 */
public class BinaryTreeRecursion {
  private BinaryTreeNode<Integer> root;

  public BinaryTreeRecursion() {
    root = new BinaryTreeNode<Integer>();
  }

  public BinaryTreeRecursion(int[] values) {
    System.out.print("构造二叉树:");
    for (int i : values) {
      System.out.print(i);
    }
    System.out.println();
    boolean isLeft = true;
    int length = values.length;
    if (length == 0)
      return;
    LinkedList<BinaryTreeNode<Integer>> queue = new LinkedList<BinaryTreeNode<Integer>>();
    root = new BinaryTreeNode<Integer>(values[0]);
    queue.addLast(root);
    BinaryTreeNode parent = null;
    BinaryTreeNode current = null;
    for (int i = 1; i < length; i++) {
      current = new BinaryTreeNode<Integer>(values[i]);
      queue.addLast(current);
      if (isLeft)
        parent = queue.getFirst();
      else
        parent = queue.removeFirst();
      if (isLeft) {
        parent.setLeftChild(current);
        isLeft = false;
      } else {
        parent.setRightChild(current);
        isLeft = true;
      }
    }
  }

  public void preorder() {
    System.out.println("二叉树递归先序遍历:");
    preorderTraverseRecursion(root);
    System.out.println();
  }

  public void inorder() {
    System.out.println("二叉树递归中序遍历:");
    inorderTraverseRecursion(root);
    System.out.println();
  }

  public void postorder() {
    System.out.println("二叉树递归后序遍历:");
    postorderTraverseRecursion(root);
    System.out.println();
  }

  /**
   * 递归先序遍历
   *
   * @param node
   * @return void
   */
  private void preorderTraverseRecursion(BinaryTreeNode<Integer> node) {
    // No.1
    if (node == null) {
      return;
    } else {
      System.out.println("node vale :" + node.getValue());
      preorderTraverseRecursion(node.getLeftChild());
      preorderTraverseRecursion(node.getRightChild());
    }
    // end_code
  }

  /**
   * 非递归先序遍历
   *
   * @param node
   * @return void
   */
  private void preorderTraverseNoRecursion(BinaryTreeNode<Integer> node) {
    Stack<BinaryTreeNode> stack = new Stack<BinaryTreeNode>();
    if (node == null) {
      return;
    }
    // 根节点入栈
    stack.push(node);
    while (!stack.isEmpty()) {
      node = stack.pop();
      System.out.println("node vale :" + node.getValue());
      if (node.getRightChild() != null) {
        stack.push(node.getRightChild());
      }
      if (node.getLeftChild() != null) {
        stack.push(node.getLeftChild());
      }
    }
  }

  /**
   * 递归中序遍历
   *
   * @param node
   * @return void
   */
  private void inorderTraverseRecursion(BinaryTreeNode<Integer> node) {
    // No.2
    // 开始写代码，用递归方法实现二叉树的中序遍历
    if (node == null) {
      return;
    } else {
      inorderTraverseRecursion(node.getLeftChild());
      System.out.println("node vale :" + node.getValue());
      inorderTraverseRecursion(node.getRightChild());
    }
    // end_code
  }

  /**
   * 非递归中序遍历,左根右
   *
   * @param node
   * @return void
   */
  private void inorderTraverseNoRecursion(BinaryTreeNode<Integer> node) {
    Stack<BinaryTreeNode> stack = new Stack<BinaryTreeNode>();
    if (node == null) {
      return;
    }
    BinaryTreeNode tmpNode = node;
    while (tmpNode != null || !stack.isEmpty()) {
      // 先将根节点和左节点循环入栈
      while (tmpNode != null) {
        stack.push(tmpNode);
        tmpNode = tmpNode.getLeftChild();
      }
      // 循环左节点和根节点出栈，然后将临时节点指向右节点
      if (!stack.isEmpty()) {
        tmpNode = stack.pop();
        System.out.println("node vale :" + tmpNode.getValue());
        tmpNode = tmpNode.getRightChild();
      }
    }
  }

  /**
   * 递归后序遍历
   *
   * @param node
   * @return void
   */
  private void postorderTraverseRecursion(BinaryTreeNode<Integer> node) {
    if (node == null) {
      return;
    } else {
      postorderTraverseRecursion(node.getLeftChild());
      postorderTraverseRecursion(node.getRightChild());
      System.out.println("node vale :" + node.getValue());
    }
  }

  /**
   * 非递归后序遍历
   *
   * @param node
   * @return void
   */
  private void postorderTraverseNoRecursion(BinaryTreeNode<Integer> node) {
    Stack<BinaryTreeNode> stack = new Stack<BinaryTreeNode>();
    if (node == null) {
      return;
    }
    BinaryTreeNode tmpNode = node;
    while (!stack.isEmpty()) {
      // 先将所有的左子树入栈
      for (; node.getLeftChild() != null; node = node.getLeftChild()) {
        stack.push(node);
      }
      // 当前节点无右子或右子已经输出
      while (node != null
          && (node.getRightChild() == null || node.getRightChild().equals(tmpNode))) {
        System.out.println("node vale :" + node.getValue());
        tmpNode = node;// 记录上一个已输出节点
        if (stack.empty())
          return;
        node = stack.pop();
      }
      // 处理右子
      stack.push(node);
      node = node.getRightChild();
    }
  }

  public static void main(String[] args) {
    BinaryTreeRecursion binaryTreeRecursion =
        new BinaryTreeRecursion(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
    binaryTreeRecursion.preorder();
    binaryTreeRecursion.inorder();
    binaryTreeRecursion.postorder();
  }
}


class BinaryTreeNode<value> {
  private value value;
  private BinaryTreeNode<value> leftChild;
  private BinaryTreeNode<value> rightChild;

  public BinaryTreeNode() {}

  public BinaryTreeNode(value value) {
    this.value = value;
    leftChild = null;
    rightChild = null;
  }

  public void setLeftChild(BinaryTreeNode<value> lNode) {
    this.leftChild = lNode;
  }

  public void setRightChild(BinaryTreeNode<value> rNode) {
    this.rightChild = rNode;
  }

  public value getValue() {
    return value;
  }

  public void setValue(value value) {
    this.value = value;
  }

  public BinaryTreeNode<value> getLeftChild() {
    return leftChild;
  }

  public BinaryTreeNode<value> getRightChild() {
    return rightChild;
  }
}
