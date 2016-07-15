package com.web.common.web.common.algorithm;

import java.util.LinkedList;
import java.util.Stack;

public class MaxDistanceNonRecursion {
    private static BinarysTreeNode<Integer> root;
    private static int maxLength;
    private static Stack<BinarysTreeNode> stack = new Stack<BinarysTreeNode>();

    public MaxDistanceNonRecursion() {
        root = new BinarysTreeNode<Integer>();
    }

    /**
     * 构造方法，初始化二叉树
     *
     * @param values
     */
    public MaxDistanceNonRecursion(int[] values) {
        System.out.print("构造二叉树:");
        for (int i : values) {
            System.out.print(i);
        }
        System.out.println();
        boolean isLeft = true;
        int length = values.length;
        if (length == 0)
            return;
        LinkedList<BinarysTreeNode<Integer>> queue = new LinkedList<BinarysTreeNode<Integer>>();
        root = new BinarysTreeNode<Integer>(values[0]);
        queue.addLast(root);
        BinarysTreeNode parent = null;
        BinarysTreeNode current = null;
        for (int i = 1; i < length; i++) {
            current = new BinarysTreeNode<Integer>(values[i]);
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

    public static void main(String args[]) {
        MaxDistanceNonRecursion maxDistanceNonRecursion =
            new MaxDistanceNonRecursion(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
        maxDistanceNonRecursion.getMax();
    }

    public static void getMax() {
        int length = getMaxLength(root);
        System.out.println("二叉树结点的最大距离为" + length);
    }

    /**
     * 二叉树结点的最大距离
     *
     * @param root
     * @return int
     */
    private static int getMaxLength(BinarysTreeNode root) {
        if (root == null)
            return 0;

        stack.push(root);
        while (!stack.isEmpty()) {
            BinarysTreeNode node = stack.peek();

            if (node.getLeftChild() == null && node.getRightChild() == null) {
                node.visited = true;
                stack.pop();
                continue;
            }
            // No.1
            // 开始写代码，求二叉树结点的最大距离，采取非递归前序遍历二叉树
            int maxLeft = 0;
            int maxRight = 0;
            if (node.getLeftChild() != null) {
                stack.push(node);
            }
            if (node.getRightChild() != null) {
                stack.push(node);
            }

            if (maxLength < maxLeft) {
                maxLength = maxLeft;
            }
            if (maxLength < maxRight) {
                maxLength = maxRight;
            }
            // end_code
        }
        return maxLength;
    }

}


/**
 * <pre>
 * 二叉树节点
 * </pre>
 */
class BinarysTreeNode<value> {
    // 节点值
    private value value;
    // 左子节点
    private BinarysTreeNode<value> leftChild;
    // 右子节点
    private BinarysTreeNode<value> rightChild;
    // 左子节点最大节点数
    public int maxLeft = 0;
    // 右节点最大节点数
    public int maxRight = 0;
    // 是否已经访问
    public boolean visited = false;

    public BinarysTreeNode() {
    }

    ;

    public BinarysTreeNode(value value) {
        this.value = value;
        leftChild = null;
        rightChild = null;
    }

    public void setLeftChild(BinarysTreeNode<value> lNode) {
        this.leftChild = lNode;
    }

    public void setRightChild(BinarysTreeNode<value> rNode) {
        this.rightChild = rNode;
    }

    public value getValue() {
        return value;
    }

    public void setValue(value value) {
        this.value = value;
    }

    public BinarysTreeNode<value> getLeftChild() {
        return leftChild;
    }

    public BinarysTreeNode<value> getRightChild() {
        return rightChild;
    }
}
