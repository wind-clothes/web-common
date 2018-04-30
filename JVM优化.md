## JVM 优化

15、JVM线上问题分类排查。
CPU问题。
       死循环：查看系统负载，排查方法：top/top H/top -Hp pid。
       线程阻塞：查看线程栈，排查方法：jstack pid。
       频繁GC：查看GC情况，排查方法：jstat -gcutil pid。

20、CPU负载飙高甚至达到100%，如何排查？
       第一步，找到CPU负载最高的进程pid：top/top -c
       第二步，找到CPU负载最高的线程pid：top -Hp pid。
       第三步，线程pid转换为十六进制：printf '%x\n' pid。
       第四步，找到Java堆栈信息：jstack 进程pid | grep 线程pid十六进制 -C10 --color。
       第五步，定位到线程信息和问题代码。

31、JVM垃圾回收器算法。
       引用计数算法，用于标记对象，不是实际采用。
       可达性分析算法，用于标记对象，是实际采用。
       标记清除算法，用于清理对象，作用于老年代。
       标记整理算法，用于清理对象，作用于老年代。
       复制算法，用于清理对象，作用于新生代。
       分代算法，堆内存，新生代，老年代，永久代。
       分区算法，新生代，Eden区，S1区，S2区。

32、JVM垃圾回收器实现。Young、Tenured、Permanent。
       新生代-Serial，复制算法，独占式串行回收器。
       新生代-ParNew，复制算法，独占式并行回收器，新生代-Serial的多线程版。
       新生代-Parallel Scavenge，复制算法，并发回收器，关注吞吐量。
       老年代-Serial Old，标记整理算法，独占式串行回收器.
       老年代-CMS，标记清除算法，并发回收器，关注STW停顿时间。
       老年代-Parallel Old，标记整理算法，并发回收器，关注吞吐量。
       新生代+老年代-G1，比较复杂。

33、JVM垃圾回收器搭配。
       搭配A：老年代-Serial Old->新生代-Serial。（-XX:+UseSerialGC）
       搭配B：老年代-Serial Old->新生代-ParNew。（-XX:+UseParNewGC）
       搭配C：老年代-Serial Old->新生代-Parallel Scavenge。（-XX:+UseParallelGC）
       搭配D：老年代-CMS->新生代-Serial，内存不足，转为A。（-XX:+UseConcMarkSweepGC、-XX:+UseSerialGC）
       搭配E：老年代-CMS->新生代-ParNew，内存不足，转为B。（-XX:+UseConcMarkSweepGC）
       搭配F：老年代-Parallel Old->新生代-Parallel Scavenge。（-XX:+UseParallelOldGC）
       搭配G：老年代-G1->新生代-G1。（-XX:+UseG1GC）

43、Full GC触发条件。
       System.gc()。
       老年代空间不足。
       老年代无连续空间。
       新生代平均晋升大于老年代剩余。
       永久代空间不足。

44、CMS GC触发条件（-XX:+UseCMSInitiatingOccupancyOnly）。
       老年代可用空间比例>=XX:CMSInitiatingOccupancyFraction阈值。
       老年代可用空间大小>=Max(新生代平均晋升大小，E区使用量+S0区使用量)。
       -XX:+CMSClassUnloadingEnabled、-XX:CMSInitiatingPermOccupancyFraction，待补充。

```
free看到的是宿主机内存情况

free -g

查看本容器使用内存

ps -aux|grep -v "RSS"|awk 'BEGIN{total=0}{total+=$6}END{print total}'

```
相关参数如下：

```shell
-XX:MaxHeapFreeRatio=70 空余堆内存大于70%时,JVM会减少堆直到 -Xms的最小限制
-XX:MaxTenuringThreshold=8 新生代经过8次minor gc后还存活则进入老年代
-XX:-UseAdaptiveSizePolicy 关闭自动调整新生代以及Survivor大小
-XX:-UseBiasedLocking  关闭偏向锁，在竞争激烈的场合,偏向锁会增加系统负担(每次都要加一次是否偏向的判断)


-XX:+UseParNewGC 使用ParNew算法收集新生代
-XX:+UseConcMarkSweepGC  使用cms收集老年代
-XX:CMSInitiatingOccupancyFraction=65 老年代占满65%开始回收
-XX:+UseCMSCompactAtFullCollection  在FULL GC的时候， 对年老代的压缩，消除碎片
-XX:CMSFullGCsBeforeCompaction=5 5次后进行内存压缩
-XX:+CMSParallelRemarkEnabled  开启并行remark，降低标记停顿
-XX:+CMSClassUnloadingEnabled  清理永久代时移除不再使用的classes

辅助：
-XX:+PrintGCDateStamps 打印gc时间
-XX:+PrintGCDetails 打印gc详情
-XX:+UseGCLogFileRotation gc日志绕接
-XX:NumberOfGCLogFiles=10  gc日志个数
-XX:GCLogFileSize=10M gc日志大小
-XX:+DisableExplicitGC 禁止代码中显式调用System.gc()

--注意netty nio框架手动触发System.gc()情形
-XX:+DisableExplicitGC调整为-XX:+ExplicitGCInvokesConcurrent（当收到System.gc()请求时使用CMS收集器进行收集）。很多NIO框架比如Netty会有很多内存映射的代码（memory map），而mmap的内存分配至不会在用Eden区或者Old区的，是属于堆外分配，因此，这些框架中会手动调用system.gc来回收mmap分配的空间（system.gc触发的是FullGC，只有FullGC时才会回收mmap分配的内存）。因为NIO框架频繁的调用FullGC会严重影响性能，因此，有人加上了这个参数：-XX:+DisableExplicitGC ，用来禁止system.gc对gc的触发。加上后system.gc将不会触发gc。但如果不允许手动调用gc，使用了NIO或者NIO框架（Mina/Netty），DirectByteBuffer分配字节缓冲区，以及MappedByteBuffer做内存映射等堆外的内存又无法回收
```


