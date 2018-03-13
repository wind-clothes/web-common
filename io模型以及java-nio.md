##IO模型以及JAVA-NIO
_______
###IO模型
概述linux中的IO模型有：BIO(阻塞式的IO),NIO(非阻塞式IO),IO多路复用,基于信号通知的IO,AIO（异步IO）

1. BIO(阻塞式的IO)

	1.1 概念描述
	
	1.2 应用场景和缺点
	
2. NIO(非阻塞式IO)

	2.1 概念描述
	
	2.2 应用场景和缺点

3. IO多路复用
4. 基于信号通知的IO
5. AIO(异步IO)

###java-Nio

概述：

####Buffer
缓冲区由四个属性指明其状态。

#####

- 容量（Capacity）：缓冲区能够容纳的数据元素的最大数量。初始设定后不能更改。 
- 上界（Limit）：缓冲区中第一个不能被读或者写的元素位置。或者说，缓冲区内现存元素的上界。 
- 位置（Position）：缓冲区内下一个将要被读或写的元素位置。在进行读写缓冲区时，位置会自动更新。 
- 标记（Mark）：一个备忘位置。初始时为“未定义”，调用mark时mark=positon，调用reset时position=mark。 
这四个属性总是满足如下关系:

```
mark<=position<=limit<=capacity

```
1. 写入和读取缓冲区中的数据
2. remaining和hasRemaining
3. Flip翻转
4. compact压缩
5. duplicate复制
6. slice缓冲区切片

####Channel
####
