## HBase

* coprocessor
### 数据存储结构

http://www.blogjava.net/DLevin/archive/2015/08/22/426877.html
http://www.blogjava.net/DLevin/archive/2015/08/22/426950.html

Row Key

与 NoSQL 数据库一样，Row Key 是用来检索记录的主键。访问 HBase table 中的行，只有三种方式：
通过单个 Row Key 访问。
通过 Row Key 的 range 全表扫描。
Row Key 可以使任意字符串（最大长度是64KB，实际应用中长度一般为 10 ~ 100bytes），在HBase 内部，Row Key 保存为字节数组。
在存储时，数据按照* Row Key 的字典序（byte order）排序存储*。设计 Key 时，要充分排序存储这个特性，将经常一起读取的行存储到一起（位置相关性）。 注意 字典序对 int 排序的结果是 1，10,100,11,12,13,14,15,16,17,18,19,20,21，…， 9，91,92,93,94,95,96,97,98,99。要保存整形的自然序，Row Key 必须用 0 进行左填充。 行的一次读写是原子操作（不论一次读写多少列）。这个设计决策能够使用户很容易理解程序在对同一个行进行并发更新操作时的行为。
列族

HBase 表中的每个列都归属于某个列族。列族是表的 Schema 的一部分（而列不是），必须在使用表之前定义。列名都以列族作为前缀，例如 courses:history、courses:math 都属于 courses 这个列族。
访问控制、磁盘和内存的使用统计都是在列族层面进行的。在实际应用中，列族上的控制权限能帮助我们管理不同类型的应用， 例如，允许一些应用可以添加新的基本数据、一些应用可以读取基本数据并创建继承的列族、一些应用则只允许浏览数据（甚至可能因为隐私的原因不能浏览所有数据）。
时间戳

HBase 中通过 Row 和 Columns 确定的一个存储单元称为 Cell。每个 Cell 都保存着同一份数据的多个版本。 版本通过时间戳来索引，时间戳的类型是 64 位整型。时间戳可以由HBase（在数据写入时自动）赋值， 此时时间戳是精确到毫秒的当前系统时间。时间戳也 可以由客户显示赋值。如果应用程序要避免数据版本冲突，就必须自己生成具有唯一性的时间戳。每个 Cell 中，不同版本的数据按照时间倒序排序，即最新的数据排在最前面。
为了避免数据存在过多版本造成的管理（包括存储和索引）负担，HBase 提供了两种数据版本回收方式。 一是保存数据的最后 n 个版本，二是保存最近一段时间内的版本（比如最近七天）。用户可以针对每个列族进行设置。
Cell

Cell 是由 {row key，column(=< family> + < label>)，version} 唯一确定的单元。Cell 中的数据是没有类型的，全部是字节码形式存储。


zookeeper的作用

保证任何时候，集群中只有一个master
存储所有Region的寻址入口
实时监控Region server的上线和下线信息。并实时通知给master
存储HBase的schema和table元数据
HMaster的作用

为Region server分配region
负责Region server的负载均衡
发现失效的Region server并重新分配其上的region。
HDFS上的垃圾文件回收。
处理schema更新请求。
HRegionServer的作用

维护master分配给他的region，处理对这些region的io请求。
负责切分正在运行过程中变的过大的region。
注意：client访问hbase上的数据时不需要master的参与，因为数据寻址访问zookeeper和region server，而数据读写访问region server。master仅仅维护table和region的元数据信息，而table的元数据信息保存在zookeeper上，因此master负载很低。


### HBase 使用场景
HBase 采用 LSM 架构，适合写多读少场景
1. HBase 适用场景
支持海量数据,需要 TB/PB 级别的在线服务（亿行以上）
拥有良好扩展性,数据量增长速度快,对水平扩展能力有需求
高性能读写,简单 kv 读写,响应延迟低
写入很频繁,吞吐量大
满足强一致性要求
批量读取数据需求
schema 灵活多变
无跨行跨表事务要求
2. HBase 的限制
只有主索引(Rowkey),不支持表联接、聚合、order by 等高级查询
3. 案例分析
A. 统一日志案例
场景:写多读少,写入量巨大(日写入>37 亿),读取随机且访问较少。
B. 一次导入,多次读取
场景:晚上导入大量数据,白天提供用户访问,每次查询是带有客户 ID 的随机数据,数据量可以估算,每 条数据<100B。
C. 监控数据
