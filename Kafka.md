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
  HW(high watermark 最高的所有副本的水平位 offset为high watermark的message并不是commit的)

* 数据的持久化



* 如何处理所有Replica都不工作

　　上文提到，在ISR中至少有一个follower时，Kafka可以确保已经commit的数据不丢失，但如果某个Partition的所有Replica都宕机了，就无法保证数据不丢失了。这种情况下有两种可行的方案：

等待ISR中的任一个Replica“活”过来，并且选它作为Leader
选择第一个“活”过来的Replica（不一定是ISR中的）作为Leader
　　这就需要在可用性和一致性当中作出一个简单的折衷。如果一定要等待ISR中的Replica“活”过来，那不可用的时间就可能会相对较长。而且如果ISR中的所有Replica都无法“活”过来了，或者数据都丢失了，这个Partition将永远不可用。选择第一个“活”过来的Replica作为Leader，而这个Replica不是ISR中的Replica，那即使它并不保证已经包含了所有已commit的消息，它也会成为Leader而作为consumer的数据源（前文有说明，所有读写都由Leader完成）。Kafka0.8.*使用了第二种方式。根据Kafka的文档，在以后的版本中，Kafka支持用户通过配置选择这两种方式中的一种，从而根据不同的使用场景选择高可用性还是强一致性。

* 如何选举Leader

　　最简单最直观的方案是，所有Follower都在Zookeeper上设置一个Watch，一旦Leader宕机，其对应的ephemeral znode会自动删除，此时所有Follower都尝试创建该节点，而创建成功者（Zookeeper保证只有一个能创建成功）即是新的Leader，其它Replica即为Follower。
　　但是该方法会有3个问题： 　　

    split-brain 这是由Zookeeper的特性引起的，虽然Zookeeper能保证所有Watch按顺序触发，但并不能保证同一时刻所有Replica“看”到的状态是一样的，这就可能造成不同Replica的响应不一致
    herd effect 如果宕机的那个Broker上的Partition比较多，会造成多个Watch被触发，造成集群内大量的调整
    Zookeeper负载过重 每个Replica都要为此在Zookeeper上注册一个Watch，当集群规模增加到几千个Partition时Zookeeper负载会过重。
　　
  Kafka 0.8.*的Leader Election方案解决了上述问题，它在所有broker中选出一个controller，所有Partition的Leader选举都由controller决定。controller会将Leader的改变直接通过RPC的方式（比Zookeeper Queue的方式更高效）通知需为此作出响应的Broker。同时controller也负责增删Topic以及Replica的重新分配。

* LeaderAndIsrRequest响应过程
　　对于收到的LeaderAndIsrRequest，Broker主要通过ReplicaManager的becomeLeaderOrFollower处理，流程如下：

  * 若请求中controllerEpoch小于当前最新的controllerEpoch，则直接返回ErrorMapping.StaleControllerEpochCode。
  * 对于请求中partitionStateInfos中的每一个元素，即（(topic, partitionId), partitionStateInfo)：
　　　2.1 若partitionStateInfo中的leader epoch大于当前ReplicManager中存储的(topic, partitionId)对应的partition的leader epoch，则：
　　　　2.1.1 若当前brokerid（或者说replicaid）在partitionStateInfo中，则将该partition及partitionStateInfo存入一个名为partitionState的HashMap中
　　　　2.1.2否则说明该Broker不在该Partition分配的Replica list中，将该信息记录于log中
　  2.2否则将相应的Errorcode（ErrorMapping.StaleLeaderEpochCode）存入Response中筛选出partitionState中Leader与当前Broker ID相等的所有记录存入partitionsTobeLeader中，其它记录存入partitionsToBeFollower中。
  * 若partitionsTobeLeader不为空，则对其执行makeLeaders方。
  * 若partitionsToBeFollower不为空，则对其执行makeFollowers方法。
  * 若highwatermak线程还未启动，则将其启动，并将hwThreadInitialized设为true。
  * 关闭所有Idle状态的Fetcher。
<img src="./ds/LeaderAndIsrRequest_Flow_Chart.png" height="200px" width="200px"></img>

* 创建/删除Topic

  * 对于删除Topic操作，Topic工具会将该Topic名字存于/admin/delete_topics。若delete.topic.enable为true，则Controller注册在/admin/delete_topics上的Watch被fire，Controller通过回调向对应的Broker发送StopReplicaRequest；若为false则Controller不会在/admin/delete_topics上注册Watch，也就不会对该事件作出反应，此时Topic操作只被记录而不会被执行。

  * 对于创建Topic操作，Controller从/brokers/ids读取当前所有可用的Broker列表，对于set_p中的每一个Partition：
　　3.1 从分配给该Partition的所有Replica（称为AR）中任选一个可用的Broker作为新的Leader，并将AR设置为新的ISR（因为该Topic是新创建的，所以AR中所有的Replica都没有数据，可认为它们都是同步的，也即都在ISR中，任意一个Replica都可作为Leader）
　　3.2 将新的Leader和ISR写入/brokers/topics/[topic]/partitions/[partition]
  * 直接通过RPC向相关的Broker发送LeaderAndISRRequest。

<img src="./ds/kafka_create_topic.png" height="200px" width="200px"></img>

* Partition重新分配
　　管理工具发出重新分配Partition请求后，会将相应信息写到/admin/reassign_partitions上，而该操作会触发ReassignedPartitionsIsrChangeListener，从而通过执行回调函数KafkaController.onPartitionReassignment来完成以下操作：

将Zookeeper中的AR（Current Assigned Replicas）更新为OAR（Original list of replicas for partition） + RAR（Reassigned replicas）。
强制更新Zookeeper中的leader epoch，向AR中的每个Replica发送LeaderAndIsrRequest。
将RAR - OAR中的Replica设置为NewReplica状态。
等待直到RAR中所有的Replica都与其Leader同步。
将RAR中所有的Replica都设置为OnlineReplica状态。
将Cache中的AR设置为RAR。
若Leader不在RAR中，则从RAR中重新选举出一个新的Leader并发送LeaderAndIsrRequest。若新的Leader不是从RAR中选举而出，则还要增加Zookeeper中的leader epoch。
将OAR - RAR中的所有Replica设置为OfflineReplica状态，该过程包含两部分。第一，将Zookeeper上ISR中的OAR - RAR移除并向Leader发送LeaderAndIsrRequest从而通知这些Replica已经从ISR中移除；第二，向OAR - RAR中的Replica发送StopReplicaRequest从而停止不再分配给该Partition的Replica。
将OAR - RAR中的所有Replica设置为NonExistentReplica状态从而将其从磁盘上删除。
将Zookeeper中的AR设置为RAR。
删除/admin/reassign_partition。
　　
注意：最后一步才将Zookeeper中的AR更新，因为这是唯一一个持久存储AR的地方，如果Controller在这一步之前crash，新的Controller仍然能够继续完成该过程。
　　以下是Partition重新分配的案例，OAR = ｛1，2，3｝，RAR = ｛4，5，6｝，Partition重新分配过程中Zookeeper中的AR和Leader/ISR路径如下

<table>
<thead>
<tr>
<th>AR</th>
<th>leader/isr</th>
<th>Step</th>
</tr>
</thead>
<tbody>
<tr>
<td>{1,2,3}</td>
<td>1/{1,2,3}</td>
<td>(initial state)</td>
</tr>
<tr>
<td>{1,2,3,4,5,6}</td>
<td>1/{1,2,3}</td>
<td>(step 2)</td>
</tr>
<tr>
<td>{1,2,3,4,5,6}</td>
<td>1/{1,2,3,4,5,6}</td>
<td>(step 4)</td>
</tr>
<tr>
<td>{1,2,3,4,5,6}</td>
<td>4/{1,2,3,4,5,6}</td>
<td>(step 7)</td>
</tr>
<tr>
<td>{1,2,3,4,5,6}</td>
<td>4/{4,5,6}</td>
<td>(step 8)</td>
</tr>
<tr>
<td>{4,5,6}</td>
<td>4/{4,5,6}</td>
<td>(step 10)</td>
</tr>
</tbody>
</table>


待解决的问题：

cap问题的原因,log文件存储的数据是什么，持久化机制。p的消息确认和fllower的ack是不是异步的。

