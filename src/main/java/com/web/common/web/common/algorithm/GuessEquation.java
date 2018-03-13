package com.web.common.web.common.algorithm;

import java.util.ArrayList;
import java.util.List;

public class GuessEquation {

    static List<String> listRepeated = new ArrayList<String>();
    static List<String> listNonRepeated = new ArrayList<String>();

    public static void initial(int[] array) {
        for (int i = 0; i < 9; i++) {
            array[i] = i + 1;
        }
    }

    public static boolean isDuplicate(String s1, String s2) {
        String a1 = s1.substring(0, 2);
        String a2 = s1.substring(2, 4);
        String b1 = s2.substring(0, 2);
        String b2 = s2.substring(2, 4);
        if (a1.equals(b2) && a2.equals(b1)) {
            return true;
        } else {
            return false;
        }
    }

    // No.1
    // 开始写代码，完成removeDuplicate方法，实现将重复算式移除的功能

    /**
     * 移除重复的结果
     *
     * @param
     * @return void
     */
    public static void removeDuplicate() {
        listNonRepeated.add(listRepeated.get(0));
        for (int i = 1; i < listRepeated.size(); i++) {
            boolean flag = true; // 标记是否重复
            for (int j = 0; j < listNonRepeated.size(); j++) {
                flag = isDuplicate(listRepeated.get(i), listNonRepeated.get(j)); // 判断是否重复
                if (flag)
                    break; // 如果元素重复,直接跳出这层循环,测试下个数据
            }
            if (!flag) {
                listNonRepeated.add(listRepeated.get(i)); // 不重复,则添加
            }
        }
    }


    // end_code

    /**
     * 打印结果
     *
     * @param
     * @return void
     */
    public static void print() {
        for (String string : listNonRepeated) {
            String leftNumber1 = string.substring(0, 2);
            String leftNumber2 = string.substring(2, 4);
            String rightNumber1 = string.substring(4, 6);
            String rightNumber2 = string.substring(6);
            System.out.println(
                leftNumber1 + " x " + leftNumber2 + " = " + rightNumber1 + " x " + rightNumber2);
        }
    }

    /**
     * 校验是否满足要求
     *
     * @param array
     * @return void
     */
    public static void check(int[] array) {
        StringBuffer stringOutput = new StringBuffer();
        for (int x : array) {
            stringOutput.append(x);
        }
        int leftNumber1 = Integer.parseInt(stringOutput.substring(0, 2).toString());
        int leftNumber2 = Integer.parseInt(stringOutput.substring(2, 4).toString());
        int rightNumber1 = Integer.parseInt(stringOutput.substring(4, 6).toString());
        int rightNumber2 = Integer.parseInt(stringOutput.substring(6).toString());
        if (leftNumber1 * leftNumber2 == rightNumber1 * rightNumber2) {
            listRepeated.add(stringOutput.toString());
        }
    }

    /**
     * <pre>
     * 进行递归排序
     * </pre>
     *
     * @param array
     * @param start
     * @param end
     */
    public static void allSort(int[] array, int start, int end) {
        if (start >= end) {
            check(array);
            return;
        } else {
            // No.2
            // 开始写代码，补充完整全排列
            for (int i = start; i <= end; i++) {
                int t = array[start]; // 交换元素
                array[start] = array[i];
                array[i] = t;
                // 递归全排列
                allSort(array, start + 1, end);
                t = array[start]; // 还原元素
                array[start] = array[i];
                array[i] = t;
            }
            // end_code
        }
    }

    public static void fun() {
        int[] array = new int[9];
        initial(array);
        allSort(array, 0, array.length - 1);
        removeDuplicate();
    }

    public static void main(String[] args) {
        fun();
        print();
    }
}
