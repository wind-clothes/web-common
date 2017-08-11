##kafaka
_____

###综合描述

###系统架构

* 组成构件
  1. Broker
　　	Kafka集群包含一个或多个服务器，这种服务器被称为broker
  2. Topic
　　每条发布到Kafka集群的消息都有一个类别，这个类别被称为Topic。（物理上不同Topic的消息分开存储，逻辑上一个Topic的消息虽然保存于一个或多个broker上但用户只需指定消息的Topic即可生产或消费数据而不必关心数据存于何处）
  3. Partition
　　Parition是物理上的概念，每个Topic包含一个或多个Partition.
  4. Producer
　  负责发布消息到Kafka broker
  5. Consumer
　　消息消费者，向Kafka broker读取消息的客户端。
  6. Consumer Group
　　每个Consumer属于一个特定的Consumer Group（可为每个Consumer指定group name，若不指定group name则属于默认的group）。

* 基本数据结构和存储机制

* 消息的路由
  在Producer进行消息传送的时候，可以根据自定义的策略来处理消息的负载均衡，是通过partition参数进行的处理的

* 分组
  Consumer Group 中一种Partition种的消息只能被该组下的一个消费者消费。

* 消息的传送保证

  ```
    At most once 消息可能会丢，但绝不会重复传输
    At least one 消息绝不会丢，但可能会重复传输
    Exactly once 每条消息肯定会被传输一次且仅传输一次，很多时候这是用户所想要的。
  ```
  针对producer端，默认是支持**At least one**的，因为当Producer向broker发送消息时，一旦这条消息被commit，因数replication的存在，它就不会丢可以通过但是Producer可以生成一种类似于主键的东西，发生故障时幂等性的重试多次

  针对consumer端，默认支持**At least one**Consumer从broker中pull消息的时候，一旦其消费完成，其将像zookeeper进行commit操作，修改offset，等下次pull消息的时候，根据offset自动消费下一条消息，如果想保证**Exactly once**可以通过业务的幂等性来处理或者需要协调offset和实际操作的输出-1.经典的做法是引入两阶段提交，2如果能让offset和操作输入存在同一个地方，比如存在同一机器的HDFS中。

###实现原理

* 高可用
  Kafka分配Replica的算法如下：

将所有Broker（假设共n个Broker）和待分配的Partition排序
将第i个Partition分配到第（i mod n）个Broker上
将第i个Partition的第j个Replica分配到第（(i + j) mod n）个Broker上

Kafka的Data Replication需要解决如下问题：

怎样Propagate(传播)消息
在向Producer发送ACK前需要保证有多少个Replica已经收到该消息
怎样处理某个Replica不工作的情况
怎样处理Failed Replica恢复回来的情况

* 高性能

  * 磁盘消息采用顺序读写
  * 支持多Disk Drive
	  可以利用硬盘的多驱动和Partition的并行处理机制来将不同的Partition挂载在不同的Disk Drive上。
  * 零拷贝
  * 序列化
	  可集成高效的序列化机制来减少网络数据量的传输。
  * 数据压缩
	  支持单条数据和批量数据的压缩
  * 合并请求

* 高伸缩（集群）

  天然分布式

* 数据一致性
  采用zookeeper来协调数据的生产和消费,Producer使用push模式将消息发布到broker,Consumer使用pull模式从broker订阅并消费消息。

* 数据的持久化
