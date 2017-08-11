##BigTable
Bigtable 是一个分布式的结构化数据存储系统,它被设计用来处理海量数据:通常是分布在数千台普通服 务器上的 PB 级的数据。
满足：适用性广泛、可扩展、高性能和高可用性。

### 数据模型
Bigtable 是一个稀疏的、分布式的、持久化存储的多维度排序 Map5。Map 的索引是行关键字、列关键字 以及时间戳;Map 中的每个 value 都是一个未经解析的 byte 数组。
* 行
* 列族
* 时间戳
* LSM TREE

### 系统的基础架构
Bigtable是建立在其它的几个Google基础构件上的。BigTable使用Google的分布式文件系统(GFS【) 17】
存储日志文件和数据文件。BigTable 集群通常运行在一个共享的机器池中,池中的机器还会运行其它的各种 

```
Scanner scanner(T);
ScanStream *stream;
stream = scanner.FetchColumnFamily(“anchor”);
stream->SetReturnAllVersions(); scanner.Lookup(“com.cnn.www”);
for (; !stream->Done(); stream->Next()) {
printf(“%s %s %lld %s\n”, scanner.RowName(),
stream->ColumnName(), stream->MicroTimestamp(), stream->Value());

}
```
￼￼￼
各样的分布式应用程序,BigTable 的进程经常要和其它应用的进程共享机器。BigTable 依赖集群管理系统来调度任务、管理共享的机器上的资源、处理机器的故障、以及监视机器的状态。

BigTable 内部存储数据的文件是 Google SSTable 格式的。SSTable 是一个持久化的、排序的、不可更改的Map 结构,而 Map 是一个 key-value 映射的数据结构,key 和 value 的值都是任意的 Byte 串。可以对 SSTable 进行如下的操作:查询与一个 key 值相关的 value,或者遍历某个 key 值范围内的所有的 key-value 对。从内 部看,SSTable 是一系列的数据块(通常每个块的大小是 64KB,这个大小是可以配置的)。SSTable 使用块索 引(通常存储在 SSTable 的最后)来定位数据块;在打开 SSTable 的时候,索引被加载到内存。每次查找都可 以通过一次磁盘搜索完成:首先使用二分查找法在内存中的索引里找到数据块的位置,然后再从硬盘读取相 应的数据块。也可以选择把整个 SSTable 都放在内存中,这样就不必访问硬盘了。

BigTable 还依赖一个高可用的、序列化的分布式锁服务组件,叫做Chubby【8】。一个 Chubby 服务包括 了 5 个活动的副本,其中的一个副本被选为 Master,并且处理请求。只有在大多数副本都是正常运行的,并 且彼此之间能够互相通信的情况下,Chubby 服务才是可用的。当有副本失效的时候,Chubby 使用 Paxos 算法 【9,23】来保证副本的一致性。Chubby 提供了一个名字空间,里面包括了目录和小文件。每个目录或者文件 可以当成一个锁,读写文件的操作都是原子的。Chubby 客户程序库提供对 Chubby 文件的一致性缓存。每个 Chubby 客户程序都维护一个与 Chubby 服务的会话。如果客户程序不能在租约到期的时间内重新签订会话的 租约,这个会话就过期失效了9。当一个会话失效时,它拥有的锁和打开的文件句柄都失效了。Chubby 客户 程序可以在文件和目录上注册回调函数,当文件或目录改变、或者会话过期时,回调函数会通知客户程序。

Bigtable 使用 Chubby 完成以下的几个任务:

1. 确保在任何给定的时间内最多只有一个活动的 Master 副本;
2. 存储 BigTable 数据的自引导指令的位置(参考 5.1 节);
3. 查找 Tablet 服务器,以及在 Tablet 服务器失效时进行善后(5.2 节);
4. 存储 BigTable 的模式信息(每张表的列族信息);
5. 以及存储访问控制列表。

如果 Chubby 长时间无法访问,BigTable 就会失效。最近我们在使用 11 个 Chubby 服务实例的 14 个 BigTable集群上测量了这个影响。由于 Chubby 不可用而导致 BigTable 中的部分数据不能访问的平均比率是 0.0047% (Chubby 不能访问的原因可能是 Chubby 本身失效或者网络问题)。单个集群里,受 Chubby 失效影响最大的 百分比是 0.0326%10。

### 优化方案

* 局部性群组
客户程序可以将多个列族组合成一个局部性群族。对Tablet中的每个局部性群组都会生成一个单独的SSTable。将通常不会一起访问的列族分割成不同的局部性群组可以提高读取操作的效率。

* 压缩
客户程序可以控制一个局部性群组的 SSTable 是否需要压缩;如果需要压缩,那么以什么格式来压缩。 每个 SSTable 的块(块的大小由局部性群组的优化参数指定)都使用用户指定的压缩格式来压缩。虽然分块 压缩浪费了少量空间22,但是,我们在只读取 SSTable 的一小部分数据的时候就不必解压整个文件了。很多客 户程序使用了“两遍”的、可定制的压缩方式。第一遍采用 Bentley and McIlroy’s 方式[6],这种方式在一个 很大的扫描窗口里对常见的长字符串进行压缩;第二遍是采用快速压缩算法,即在一个 16KB 的小扫描窗口 中寻找重复数据。两个压缩的算法都很快,在现在的机器上,压缩的速率达到 100-200MB/s,解压的速率达 到 400-1000MB/s。

* 通过缓存提高读操作的性能

	为了提高读操作的性能,Tablet 服务器使用二级缓存的策略。扫描缓存是第一级缓存,主要缓存 Tablet服务器通过 SSTable 接口获取的 Key-Value 对;Block 缓存是二级缓存,缓存的是从GFS读取的SSTable的Block。对于经常要重复读取相同数据的应用程序来说,扫描缓存非常有效（时间局部性）;对于经常要读取刚刚读过的数据 附近的数据的应用程序来说,Block 缓存更有用(例如,顺序读,或者在一个热点的行的局部性群组中随机读取不同的列)(空间局部性)。
	+ Bloom 过滤器
    
    	如 6.3 节所述,一个读操作必须读取构成 Tablet 状态的所有 SSTable 的数据。如果这些 SSTable 不在内存 中,那么就需要多次访问硬盘。我们通过允许客户程序对特定局部性群组的 SSTable 指定 Bloom 过滤器【7】, 来减少硬盘访问的次数。我们可以使用 Bloom 过滤器查询一个 SSTable 是否包含了特定行和列的数据。对于 某些特定应用程序,我们只付出了少量的、用于存储 Bloom 过滤器的内存的代价,就换来了读操作显著减少 的磁盘访问的次数。使用 Bloom 过滤器也隐式的达到了当应用程序访问不存在的行或列时,大多数时候我们 都不需要访问硬盘的目的。

    + Commit 日志的实现

    + Tablet 恢复提速
    
        当 Master 服务器将一个 Tablet 从一个 Tablet 服务器移到另外一个 Tablet 服务器时,源 Tablet 服务器会对 这个 Tablet 做一次 Minor Compaction。这个 Compaction 操作减少了 Tablet 服务器的日志文件中没有归并的记 录,从而减少了恢复的时间。Compaction 完成之后,该服务器就停止为该 Tablet 提供服务。在卸载 Tablet 之 前,源 Tablet 服务器还会再做一次(通常会很快)Minor Compaction,以消除前面在一次压缩过程中又产生的 未归并的记录。第二次 Minor Compaction 完成以后,Tablet 就可以被装载到新的 Tablet 服务器上了,并且不 需要从日志中进行恢复。
    + 利用不变性

        我们在使用 Bigtable 时,除了 SSTable 缓存之外的其它部分产生的 SSTable 都是不变的,我们可以利用这 一点对系统进行简化。例如,当从 SSTable 读取数据的时候,我们不必对文件系统访问操作进行同步。这样 一来,就可以非常高效的实现对行的并行操作。memtable 是唯一一个能被读和写操作同时访问的可变数据结 构。为了减少在读操作时的竞争,我们对内存表采用 COW(Copy-on-write)机制,这样就允许读写操作并行执 行。
因为 SSTable 是不变的,因此,我们可以把永久删除被标记为“删除”的数据的问题,转换成对废弃的 SSTable 进行垃圾收集的问题了。每个 Tablet 的 SSTable 都在 METADATA 表中注册了。Master 服务器采用“标 记-删除”的垃圾回收方式删除 SSTable 集合中废弃的 SSTable【25】,METADATA 表则保存了 Root SSTable 的集合。
最后,SSTable 的不变性使得分割 Tablet 的操作非常快捷。我们不必为每个分割出来的 Tablet 建立新的 SSTable 集合,而是共享原来的 Tablet 的 SSTable 集合。
