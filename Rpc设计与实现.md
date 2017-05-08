###分层设计
序列化层、函数调用层、网络传输层和服务器端处理框架

1. 函数调用层：通过设置代理，来模拟rpc调用创建client，或者clientPOOL来调用底层的代码

2. 序列化层：在client中，进行消息的编解码和序列化。

3. 网络传输层：交给netty处理

如何实现client的多路复用：

1. 传统的方式：使用client,每次发出请求都信创建一个client进行连接处理

2. 使用连接池：自己实现连接池，类似于数据库连接池（池化）

3. 使用单一client，但是在request中生成一个唯一的messageId，可以是nanoTime。然后server处理完后，在response中也返回这个messageId。这样在client不是维护一个BlockingQueue，而是维护一个ConcurrentHashMap，key是messageId，value是一个空的BlockingQueue。当client发送resquest到server时，在Map里写入messageId，并实例化一个BlockingQueue（为了优化，size可以是1）。然后等待这个BlockingQueue有值。在接收到response的回调方法里，根据messageId取出blockingQueue的值，然后删除掉这个Key。




从主线程池中随机选择一个Reactor线程作为Acceptor线程，用于绑定监听端口，接收客户端连接；

Acceptor线程接收客户端连接请求之后创建新的SocketChannel，将其注册到主线程池的其它Reactor线程上，由其负责接入认证、IP黑白名单过滤、握手等操作；

步骤2完成之后，业务层的链路正式建立，将SocketChannel从主线程池的Reactor线程的多路复用器上摘除，重新注册到Sub线程池的线程上，用于处理I/O的读写操作。

###Nio框架设计与实现
----
####理想的NIO框架的特点

* 优雅地隔离IO代码和业务代码
* 易于扩展
* 易于配置,包括框架自身参数和协议参数
* 提供良好的codec框架,方便marshall/unmarshall 
* 透明性,内置良好的日志记录和数据统计
* 高性能


####Nio框架的关键因素
1. 数据的拷贝
   减少数据拷贝
     * ByteBuffer的选择
     * View ByteBuffer
     * FileChannel.transferTo/transferFrom 
     * FileChannel.map/MappedByteBuffer
   上下文切换
     *  时间缓存
     *  Selector.wakeup
     *  提高IO读写效率
     *  线程模型
     
2. 内存管理
   * Java能做的事情有限
          GC带来的自动内存管理
   * 缓冲区的管理
      *  池化
           *  ThreadLocal cache
           *  环形缓冲区
      *  扩展
           *  putString,getString等高级API
           *  缓冲区自动扩展和收缩,处理不定长度字节
      *  字节序
           * 跨语言通讯需要注意
           *  网络字节序-BigEnd
           *  默认缓冲区-BigEnd
           *  Java的IO库和class文件-BigEnd
           
3. 数据结构的选择
   *  使用简单的数据结构
      *  链表、队列、数组、散列表
   *  使用j.u.c框架引入的并发集合类
      *  lock-free,spin lock
   *  任何数据结构都要注意容量限制
      *  OutOfMemoryError
   *  适当选择数据结构的初始容量 
      *  降低GC带来的影响
   *  TCP选项、高级IO函数
   *  框架设计


8.大数据目前是发展趋势，喜欢的话，从hadoop家族开始去了解，全文检索lucene、solr，机器学习语言R、mahout、spark MR等。storm、spark、云计算平台docker、openstack等
9.对互联网行业感兴趣，需要深入了解java并发编程、消息队列、网络编程（socket、NIO、Netty）、JVM调优等方面下手。
Erlang
