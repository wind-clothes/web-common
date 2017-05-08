package com.web.common.web.common.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 * 快速排序
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2016年7月27日 下午6:30:08
 */
public class Sort {

  private static int[] a = {10, 8, 29, 45, 18, 5, 3, 7, 2};

  public static void main(String[] args) {
    quick_sort(a);
    for (int a : a) {
      System.out.print(a + " ");
    }
  }

  /**
   * 基本思想：选择一个基准元素,通常选择第一个元素或者最后一个元素, 通过一趟扫描，将待排序列分成两部分,一部分比基准元素小,一部分大于等于基准元素,
   * 此时基准元素在其排好序后的正确位置,然后再用同样的方法递归地排序划分的两部分。
   * 
   * 快速排序的时间复杂度为O(nlogn)。 当n较大时使用快排比较好，当序列基本有序时用快排反而不好。 稳定性： 不稳定的排序
   * 
   * @param array
   * @return void
   */
  public static void quick_sort(int[] array) {
    if (array == null || array.length == 0) {
      throw new IllegalArgumentException();
    }
    if (array.length <= 7) {
      insert_sort(array);
      return;
    }
    quickSort(array, 0, array.length - 1);
  }

  private static void quickSort(int[] a, int low, int high) {
    // 优化2：当数组的大小没有超过一定的阈值的时候，就使用插入排序，插入排序在数组较小的时候，性能较优化。
    if (a == null)
      return;

    if (low < high) { // 如果不加这个判断递归会无法退出导致堆栈溢出异常
      int middle = partition(a, low, high);
      if (middle > low)
        quickSort(a, low, middle - 1);
      if (middle < high)
        quickSort(a, middle + 1, high);
    }
  }

  /**
   * 一次递归调用，返回中间值索引，使得该索引的左边的值都是小于该值，右边的值都是大于该索引value
   * 
   * @return int
   */
  public static int partition(int[] a, int low, int high) {
    // 每次选取第一个元素为比较值，后期需要增加随机性
    // 比如采取抽样算法，来增加该值的随机性
    int index = low;

    int privot = a[index];

    int i = low;

    for (int j = low; j <= high; j++) {
      if (privot > a[j]) {
        swap(a, i++, j);
      }
    }
    swap(a, i, high);
    return i;
  }

  public static int partition1(int[] a, int low, int high) {
    // 每次选取第一个元素为比较值，后期需要增加随机性
    // 比如采取抽样算法，来增加该值的随机性 [10, 8, 29, 45, 18, 5, 3, 7, 2]
    // 优化1：pivot的选择随机化，不再是固定选择左端点，而是利用随机数产生器选择一个有效的位置作为pivot
    int index = randomIndex(low, high);
    // 交换pivot和左端点
    swap(a, low, index);

    // 以下是划分算法
    // int i = low;
    // for (int j = low + 1; j <= high; j++) {
    // if (a[j] < a[low]) {
    // swap(a, ++i, j);
    // }
    // }
    // swap(a, low, i);

    // 优化3:更进一步的优化是在划分算法上，现在的划分算法只使用了一个索引i，
    // i从左向右扫描，遇到比pivot小的，就跟从p+1开始的位置（由j索引进行递增标 志）进行交换，
    // 最终的划分点落在了j，然后将pivot调换到j上，再递归排序左右两边子序列。
    // 一个更高效的划分过程是使用两个索引i和j，分别从左右两 端进行扫描，
    // i扫描到大于等于pivot的元素就停止，j扫描到小于等于pivot的元素也停止，
    // 交换两个元素，持续这个过程直到两个索引相遇，
    // 此时的 pivot的位置就落在了j，然后交换pivot和j的位置，后续的工作没有不同.
    // 以下是划分算法的优化
    int i = low;// 指向左端点
    int j = high + 1;// 指向右端点
    while (true) {
      do {
        i++;
      } while (i < high && a[low] > a[i]);
      do {
        j--;
      } while (j > low && a[low] < a[j]);
      if (j < i) {
        break;
      }
      swap(a, i, j);
    }
    swap(a, low, j);
    return j;
  }

  /**
   * 1、基本思想：每步将一个待排序的记录， 按其顺序码大小插入到前面已经排序的字序列的合适位置（从后向前找到合适位置后）， 直到全部插入排序完为止。
   * 
   * 时间复杂度: 最好的情况：O(n); 最坏的情况：O(n2); 稳定性：稳定，比较的是序列前后数据的位置是否变化，稳定的；
   * 
   * @param array
   * @return void
   */
  public static void insert_sort(int[] array) {
    if (array == null || array.length <= 0) {
      return;
    }
    for (int i = 0; i < array.length; i++) {
      for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {
        swap(array, j - 1, j);
      }
    }
  }

  public static void bubble_sort2(int[] array) {
    if (array == null || array.length == 0) {
      throw new IllegalArgumentException();
    }
    int n = array.length;
    boolean flag = true;
    for (int i = 0; i < n; i++) {
      if (!flag)
        return;
      for (int j = n - 1; j > i; j--) {
        if (array[j] < array[j - 1]) {
          swap(array, j - 1, j);
          flag = true;
        }
      }
    }
  }

  /**
   * 其思想是通过无序区中相邻记录关键字间的比较和位置的交换, 使关键字最小的记录如气泡一般逐渐往上“漂浮”直至“水面” 时间复杂度： 最坏情况：逆序有序,O(n2);
   * 最好情况：正序有序，O（n）; 稳定性： 由于比较的是左右两个元素的大小，且可能不需要调换位置，故其是稳定性的排序算法。
   * 
   * @param array
   * @return void
   */
  public static void bubble_sort(int[] array) {
    if (array == null || array.length == 0) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < array.length - 1; i++) {
      for (int j = i; j < array.length; j++) {
        if (array[i] > array[j]) {
          swap(array, i, j);
        }
      }
    }
  }

  /**
   * 其思想是将序列先进行排序选出最小的一个数据放在首部， 然后对剩下的数据进行选出最小数据的操作，直至所有数据排序完成，
   * 其实具体实现的时候是将选出的最小数据放在剩余序列的首部，以此类推，直至完成。
   * 
   * 时间复杂度: 最好的情况：O(n2); 最坏的情况：O(n2); 稳定性： 由于每次都是选取未排序序列A中的最小元素x与A中的第一个元素交换，
   * 因此跨距离了，很可能破坏了元素间的相对位置，因此选择排序是不稳定的！
   * 
   * @param a
   * @return void
   */
  public static void selection_sort(int[] a) {
    if (a == null || a.length == 0) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < a.length - 1; i++) {
      int min = i;
      for (int j = i + 1; j < a.length; j++) {
        if (a[min] > a[j]) {
          min = j;
        }
      }
      swap(a, min, i);
    }
  }

  /**
   * 思想是多次将两个或两个以上的有序表合并成一个新的有序表。 算法时间复杂度
   * 最好的情况下：一趟归并需要n次，总共需要logN次，因此为O(N*logN)
   * 最坏的情况下，接近于平均情况下，为O(N*logN) 说明：对长度为n的文件，需进行logN 趟二路归并，
   * 每趟归并的时间为O(n)，故其时间复杂度无论是在最好情况下还是在最坏情况下均是O(nlgn)。
   * 稳定性： 归并排序最大的特色就是它是一种稳定的排序算法。归并过程中是不会改变元素的相对位置的。
   * 缺点是，它需要O(n)的额外空间。但是很适合于多链表排序。
   * 
   * @return void
   */
  public static void merge_sort(int[] a) {
    if (a == null || a.length <= 0) {
      return;
    }
    merge_sort(a, 0, a.length - 1);
  }

  public static void merge_sort(int[] a, int low, int high) {
    if (low < high) {
      int m = (low + high) / 2;
      // 左边合并排序
      merge_sort(a, low, m);
      // 右边合并排序
      merge_sort(a, m + 1, high);
      // 左右合并
      merge_sort(a, low, m, high);
    }
  }

  public static void merge_sort(int[] a, int low, int m, int high) {
    int[] tmpArr = new int[a.length];
    int mid = m + 1; // 右边的起始位置
    int tmp = low;
    int third = low;
    while (low <= m && mid <= high) {
      // 从两个数组中选取较小的数放入中间数组
      if (a[low] <= a[mid]) {
        tmpArr[third++] = a[low++];
      } else {
        tmpArr[third++] = a[mid++];
      }
    }
    // 将剩余的部分放入中间数组
    while (low <= m) {
      tmpArr[third++] = a[low++];
    }
    while (mid <= high) {
      tmpArr[third++] = a[mid++];
    }
    // 将中间数组复制回原数组
    while (tmp <= high) {
      a[tmp] = tmpArr[tmp++];
    }
  }

  /**
   * 希尔排序也是一种插入排序方法,实际上是一种分组插入方法。先取定一个小于n的整数d1作为第一个增量,
   * 把表的全部记录分成d1个组,所有距离为d1的倍数的记录放在同一个组中,在各组内进行直接插入排序；
   * 然后,取第二个增量d2(＜d1),重复上述的分组和排序,
   * 直至所取的增量dt=1(dt<dt+1<…<d2<d1), 即所有记录放在同一组中进行直接插入排序为止
   * 
   * 时间复杂度：
   * 最好情况：由于希尔排序的好坏和步长d的选择有很多关系，因此，目前还没有得出最好的步长如何选择(现在有些比较好的选择了，但不确定是否是最好的)。
   * 最坏情况下：O(N*logN)，最坏的情况下和平均情况下差不多。 平均情况下：O(N*logN) 稳定性： 不稳定算法
   * 
   * @return void
   */
  public static void shell_sort(int[] a) {
    int d = a.length;
    while (true) {
      d = d/2;
      for (int x = 0; x < d; x++) {//d组
        //每一组内进行直接插入排序10, 8, 29, 45, 18, 5, 3, 7, 2
        for (int i = x; i < a.length; i = i + d) {
          for (int j = i-d; j >= 0 && a[j] > a[j+d]; j = j-d) {
            swap(a, j+d, j);
          }
        }
      }
      if (d == 1) {
        break;
      }
    }
  }

  /**
   * 其思想是建立在直接选择排序思想基础之上的，并结合了完全二叉树的思想， 由于利用完全二叉树中双亲节点和孩子节点之间的内在关系， 在当前无序区中选择关键字最大(或者最小)的记录。
   * 也就是说，以最小堆为例，根节点为最小元素，较大的节点偏向于分布在堆底附近。
   * 每一趟排序的基本操作是：将当前无序区调整为一个大根堆，选取关键字最大的堆顶记录，
   * 将它和无序区中的最后一个记录交换。这样，正好和直接选择排序相反，有序区
   * 是在原记录区的尾部形成并逐步向前扩大到整个记录区。
   * 时间复杂度： 最坏情况下，接近于最差情况下：O(N*logN)，因此它是一种效果不错的排序算法。 稳定性： 堆排序需要不断地调整堆，因此它是一种不稳定的排序！
   * 10, 8, 29, 45, 18, 5, 3, 7, 2
   * 
   * [45, 18, 29, 8, 10, 5, 3, 7, 2]
   * @return void
   */
  public static void heap_sort(int[] a) {
    for (int i = 0; i < a.length - 1; i++) {
      build_max_heap(a, a.length - i - 1);
      swap(a, 0, a.length - i - 1);
    }
  }

  // 构造最大堆
  /**
   * 生成最大堆：最大堆通常都是一棵完全二叉树，因此我们使用数组的形式来存储最大堆的值，
   * 从1号单元开始存储，因此父结点跟子结点的关系就是两倍的关系。
   * 即：heap[father * 2] = heap[leftChild];  heap[father * 2 + 1] = heap[rightChild];
   * 
   * 最大堆的初始化
   * 在生成最大堆时，我们可以采取一次次遍历整棵树找到最大的结点放到相应的位置中。
   * 但是这种方法有个不好的地方，就是每个结点都要重复比较多次。
   * 所以我们可以换一种思考的顺序，从下往上进行比较。先找到最后一个有子结点的结点，
   * 先让他的两个子结点进行比较，找到大的值再和父结点的值进行比较。如果父结点的
   * 值小，则子结点和父结点进行交换，交换之后再往下比较。然后一步步递归上去，
   * 知道所在结点的位置是0号位置跳出。这样就可以有效地减少比较所用到的时间。
   * @param a
   * @param high
   * @return void
   */
  public static void build_max_heap(int[] a, int high) {
    // 从lastIndex处节点（最后一个节点）的父节点开始
    for (int i = (high - 1) / 2; i >= 0; i--) {
      // k保存正在判断的节点
      int k = i;
      // 如果当前k节点的子节点存在
      while (k * 2 + 1 <= high) {
        // k节点的左子节点的索引
        int biggerIndex = 2 * k + 1;
        // 如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在
        if (biggerIndex < high) {
          // 若果右子节点的值较大
          if (a[biggerIndex] < a[biggerIndex + 1]) {
            // biggerIndex总是记录较大子节点的索引
            biggerIndex++;
          }
        }
        // 如果k节点的值小于其较大的子节点的值
        if (a[k] < a[biggerIndex]) {
          // 交换他们
          swap(a, k, biggerIndex);
          // 将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值
          k = biggerIndex;
        } else {
          break;
        }
      }
    }
  }
  
  /**
   * 基数排序： 基本思想：将所有待比较数值（正整数）统一为同样的数位长度， 数位较短的数前面补零。然后，从最低位开始，依次进行一次排序。
   * 这样从最低位排序一直到最高位排序完成以后,数列就变成一个有序序列。
   * 
   * 算法的时间复杂度 分配需要O(n),收集为O(r),其中r为分配后链表的个数， 以r=10为例，则有0～9这样10个链表来将原来的序列分类。
   * 而d，也就是位数(如最大的数是1234，位数是4，则d=4)，即'分配-收集'的趟数。 因此时间复杂度为O(d*(n+r))。 最坏情况运行时间：O((n+k)d)
   * 最好情况运行时间：O((n+k)d) 稳定性: 基数排序过程中不改变元素的相对位置，因此是稳定的！
   * 
   * 
   * 
   * @return void
   */
  public static void radix_sort(int[] a) {
    // 找到最大数，确定要排序几趟
    int max = 0;
    for (int i = 0; i < a.length; i++) {
      if (max < a[i]) {
        max = a[i];
      }
    }
    // 判断位数
    int times = 0;
    while (max > 0) {
      max = max / 10;
      times++;
    }
    // 建立十个队列
    List<ArrayList> queue = new ArrayList<ArrayList>();
    for (int i = 0; i < 10; i++) {
      ArrayList<Integer> queue1 = new ArrayList<Integer>();
      queue.add(queue1);
    }
    // 进行times次分配和收集
    for (int i = 0; i < times; i++) {
      // 分配
      for (int j = 0; j < a.length; j++) {
        // 10, 8, 29, 45, 18, 5, 3, 7, 2 
        int x = a[j] % (int) Math.pow(10, i + 1) / (int) Math.pow(10, i);
        ArrayList<Integer> queue2 = queue.get(x);
        queue2.add(a[j]);
        queue.set(x, queue2);
      }
      // 收集,[[10], [], [2], [3], [], [45, 5], [], [7], [8, 18], [29]]
      // [10, 2, 3, 45, 5, 7, 8, 18, 29]
      // [[2, 3, 5, 7, 8], [10, 18], [29], [], [45], [], [], [], [], []]
      // [2, 3, 5, 7, 8, 10, 18, 29, 45]
      int count = 0;
      for (int j = 0; j < 10; j++) {
        while (queue.get(j).size() > 0) {
          ArrayList<Integer> queue3 = queue.get(j);
          a[count] = queue3.get(0);
          queue3.remove(0);
          count++;
        }
      }
    }
  }

  public static void swap(int[] a, int p, int q) {
    int temp = a[p];
    a[p] = a[q];
    a[q] = temp;
  }

  public static int randomIndex(int low, int high) {
    return low + new Random().nextInt(high - low + 1);
  }
}
