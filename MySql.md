## MySql

### 锁

InnoDB实现了以下两种类型的行锁。
*  共享锁（S）：允许一个事务去读一行，阻止其他事务获得相同数据集的排他锁。
*  排他锁（X)允许获得排他锁的事务更新数据，阻止其他事务取得相同数据集的共享读锁和排他写锁。

另外，为了允许行锁和表锁共存，实现多粒度锁机制，InnoDB还有两种内部使用的意向锁（Intention Locks），这两种意向锁都是表锁。

*  意向共享锁（IS）：事务打算给数据行加行共享锁，事务在给一个数据行加共享锁前必须先取得该表的IS锁。
*  意向排他锁（IX）：事务打算给数据行加行排他锁，事务在给一个数据行加排他锁前必须先取得该表的IX锁。


<table border="1" cellspacing="0" cellpadding="0" width="581"><tbody><tr><td valign="top"><div>请求锁模式</div><div>&nbsp;&nbsp; 是否兼容</div><div>当前锁模式</div></td><td><div>X</div></td><td><div>IX</div></td><td><div>S</div></td><td><div>IS</div></td></tr><tr><td valign="top"><div>X</div></td><td valign="top"><div>冲突</div></td><td valign="top"><div>冲突</div></td><td valign="top"><div>冲突</div></td><td valign="top"><div>冲突</div></td></tr><tr><td valign="top"><div>IX</div></td><td valign="top"><div>冲突</div></td><td valign="top"><div>兼容</div></td><td valign="top"><div>冲突</div></td><td valign="top"><div>兼容</div></td></tr><tr><td valign="top"><div>S</div></td><td valign="top"><div>冲突</div></td><td valign="top"><div>冲突</div></td><td valign="top"><div>兼容</div></td><td valign="top"><div>兼容</div></td></tr><tr><td valign="top"><div>IS</div></td><td valign="top"><div>冲突</div></td><td valign="top"><div>兼容</div></td><td valign="top"><div>兼容</div></td><td valign="top"><div>兼容</div></td></tr></tbody></table>

#### 锁实现
InnoDB行锁是通过给索引上的索引项加锁来实现的，这一点MySQL与Oracle不同，后者是通过在数据块中对相应数据行加锁来实现的。InnoDB这种行锁实现特点意味着：只有通过索引条件检索数据，InnoDB才使用行级锁，否则，InnoDB将使用表锁！
在实际应用中，要特别注意InnoDB行锁的这一特性，不然的话，可能导致大量的锁冲突，从而影响并发性能。下面通过一些实际例子来加以说明。

* 在不通过索引条件查询的时候，InnoDB确实使用的是表锁，而不是行锁。

* 由于MySQL的行锁是针对索引加的锁，不是针对记录加的锁，所以虽然是访问不同行的记录，但是如果是使用相同的索引键，是会出现锁冲突的。应用设计的时候要注意这一点。

* 当表有多个索引的时候，不同的事务可以使用不同的索引锁定不同的行，另外，不论是使用主键索引、唯一索引或普通索引，InnoDB都会使用行锁来对数据加锁。

* 即便在条件中使用了索引字段，但是否使用索引来检索数据是由MySQL通过判断不同执行计划的代价来决定的，如果MySQL认为全表扫描效率更高，比如对一些很小的表，它就不会使用索引，这种情况下InnoDB将使用表锁，而不是行锁。因此，在分析锁冲突时，别忘了检查SQL的执行计划，以确认是否真正使用了索引。

**间隙锁**
行锁（record Lock）锁索引记录
间隙锁 (GAP Lock)  锁数据范围，针对事务中的可重复读
Next-Key锁 (record Lock + GAP Lock) 

当我们用范围条件而不是相等条件检索数据，并请求共享或排他锁时，InnoDB会给符合条件的已有数据记录的索引项加锁；对于键值在条件范围内但并不存在的记录，叫做“间隙（GAP)”，InnoDB也会对这个“间隙”加锁，这种锁机制就是所谓的间隙锁（Next-Key锁）。

**什么时候使用表锁**
* 第一种情况是：事务需要更新大部分或全部数据，表又比较大，如果使用默认的行锁，不仅这个事务执行效率低，而且可能造成其他事务长时间锁等待和锁冲突，这种情况下可以考虑使用表锁来提高该事务的执行速度。
* 第二种情况是：事务涉及多个表，比较复杂，很可能引起死锁，造成大量事务回滚。这种情况也可以考虑一次性锁定事务涉及的表，从而避免死锁、减少数据库因事务回滚带来的开销。

**在InnoDB下，使用表锁要注意以下两点。**

* 使用LOCK TABLES虽然可以给InnoDB加表级锁，但必须说明的是，表锁不是由InnoDB存储引擎层管理的，而是由其上一层──MySQL Server负责的，仅当autocommit=0、innodb_table_locks=1（默认设置）时，InnoDB层才能知道MySQL加的表锁，MySQL Server也才能感知InnoDB加的行锁，这种情况下，InnoDB才能自动识别涉及表级锁的死锁；否则，InnoDB将无法自动检测并处理这种死锁。有关死锁，下一小节还会继续讨论。
* 在用 LOCK TABLES对InnoDB表加锁时要注意，要将AUTOCOMMIT设为0，否则MySQL不会给表加锁；事务结束前，不要用UNLOCK TABLES释放表锁，因为UNLOCK TABLES会隐含地提交事务；COMMIT或ROLLBACK并不能释放用LOCK TABLES加的表级锁，必须用UNLOCK TABLES释放表锁。

* 更新丢失（Lost Update）：当两个或多个事务选择同一行，然后基于最初选定的值更新该行时，由于每个事务都不知道其他事务的存在，就会发生丢失更新问题－－最后的更新覆盖了由其他事务所做的更新。例如，两个编辑人员制作了同一文档的电子副本。每个编辑人员独立地更改其副本，然后保存更改后的副本，这样就覆盖了原始文档。最后保存其更改副本的编辑人员覆盖另一个编辑人员所做的更改。如果在一个编辑人员完成并提交事务之前，另一个编辑人员不能访问同一文件，则可避免此问题。

* 脏读（Dirty Reads）：一个事务正在对一条记录做修改，在这个事务完成并提交前，这条记录的数据就处于不一致状态；这时，另一个事务也来读取同一条记录，如果不加控制，第二个事务读取了这些“脏”数据，并据此做进一步的处理，就会产生未提交的数据依赖关系。这种现象被形象地叫做"脏读"。

* 不可重复读（Non-Repeatable Reads）：一个事务在读取某些数据后的某个时间，再次读取以前读过的数据，却发现其读出的数据已经发生了改变、或某些记录已经被删除了！这种现象就叫做“不可重复读”。

* 幻读（Phantom Reads）：一个事务按相同的查询条件重新读取以前检索过的数据，却发现其他事务插入了满足其查询条件的新数据，这种现象就称为“幻读”。

**死锁**
上文讲过，MyISAM表锁是deadlock free的，这是因为MyISAM总是一次获得所需的全部锁，要么全部满足，要么等待，因此不会出现死锁。但在InnoDB中，除单个SQL组成的事务外，锁是逐步获得的，这就决定了在InnoDB中发生死锁是可能的。如表20-17所示的就是一个发生死锁的例子。

发生死锁后，InnoDB一般都能自动检测到，并使一个事务释放锁并回退，另一个事务获得锁，继续完成事务。但在涉及外部锁，或涉及表锁的情况下，InnoDB并不能完全自动检测到死锁，这需要通过设置锁等待超时参数 innodb_lock_wait_timeout来解决。需要说明的是，这个参数并不是只用来解决死锁问题，在并发访问比较高的情况下，如果大量事务因无法立即获得所需的锁而挂起，会占用大量计算机资源，造成严重性能问题，甚至拖跨数据库。我们通过设置合适的锁等待超时阈值，可以避免这种情况发生。

* 在应用中，如果不同的程序会并发存取多个表，应尽量约定以相同的顺序来访问表，这样可以大大降低产生死锁的机会。在下面的例子中，由于两个session访问两个表的顺序不同，发生死锁的机会就非常高！但如果以相同的顺序来访问，死锁就可以避免。
* 在程序以批量方式处理数据的时候，如果事先对数据排序，保证每个线程按固定的顺序来处理记录，也可以大大降低出现死锁的可能。
* 在事务中，如果要更新记录，应该直接申请足够级别的锁，即排他锁，而不应先申请共享锁，更新时再申请排他锁，因为当用户申请排他锁时，其他事务可能又已经获得了相同记录的共享锁，从而造成锁冲突，甚至死锁。
* 前面讲过，在REPEATABLE-READ隔离级别下，如果两个线程同时对相同条件记录用SELECT...FOR UPDATE加排他锁，在没有符合该条件记录情况下，两个线程都会加锁成功。程序发现记录尚不存在，就试图插入一条新记录，如果两个线程都这么做，就会出现死锁。这种情况下，将隔离级别改成READ COMMITTED，就可避免问题。
* 当隔离级别为READ COMMITTED时，如果两个线程都先执行SELECT...FOR UPDATE，判断是否存在符合条件的记录，如果没有，就插入记录。此时，只有一个线程能插入成功，另一个线程会出现锁等待，当第1个线程提交后，第2个线程会因主键重出错，但虽然这个线程出错了，却会获得一个排他锁！这时如果有第3个线程又来申请排他锁，也会出现死锁。




<table border="1" cellspacing="0" cellpadding="0"><tbody><tr><td valign="top" colspan="2"><div>隔离级别</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 一致性读和锁</div><div>SQL</div></td><td valign="top"><div>Read Uncommited</div></td><td valign="top"><div>Read Commited</div></td><td valign="top"><div>Repeatable Read</div></td><td valign="top"><div>Serializable</div></td></tr><tr><td valign="top"><div>SQL</div></td><td valign="top"><div>条件</div></td><td valign="top">&nbsp;</td><td valign="top">&nbsp;</td><td valign="top">&nbsp;</td><td valign="top">&nbsp;</td></tr><tr><td rowspan="2"><div>select</div></td><td valign="top"><div>相等</div></td><td valign="top"><div>None locks</div></td><td valign="top"><div>Consisten read/None lock</div></td><td valign="top"><div>Consisten read/None lock</div></td><td valign="top"><div>Share locks</div></td></tr><tr><td valign="top"><div>范围</div></td><td valign="top"><div>None locks</div></td><td valign="top"><div>Consisten read/None lock</div></td><td valign="top"><div>Consisten read/None lock</div></td><td valign="top"><div>Share Next-Key</div></td></tr><tr><td rowspan="2"><div>update</div></td><td valign="top"><div>相等</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>Exclusive locks</div></td></tr><tr><td valign="top"><div>范围</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td></tr><tr><td><div>Insert</div></td><td valign="top"><div>N/A</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td></tr><tr><td rowspan="2"><div>replace</div></td><td valign="top"><div>无键冲突</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td></tr><tr><td valign="top"><div>键冲突</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td></tr><tr><td rowspan="2"><div>delete</div></td><td valign="top"><div>相等</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td></tr><tr><td valign="top"><div>范围</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td></tr><tr><td rowspan="2"><div>Select ... from ... Lock in share mode</div></td><td valign="top"><div>相等</div></td><td valign="top"><div>Share locks</div></td><td valign="top"><div>Share locks</div></td><td valign="top"><div>Share locks</div></td><td valign="top"><div>Share locks</div></td></tr><tr><td valign="top"><div>范围</div></td><td valign="top"><div>Share locks</div></td><td valign="top"><div>Share locks</div></td><td valign="top"><div>Share Next-Key</div></td><td valign="top"><div>Share Next-Key</div></td></tr><tr><td rowspan="2"><div>Select * from ... For update</div></td><td valign="top"><div>相等</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>exclusive locks</div></td></tr><tr><td valign="top"><div>范围</div></td><td valign="top"><div>exclusive locks</div></td><td valign="top"><div>Share locks</div></td><td valign="top"><div>exclusive next-key</div></td><td valign="top"><div>exclusive next-key</div></td></tr><tr><td rowspan="2"><div>Insert into ... Select ...</div><div>（指源表锁）</div></td><td valign="top"><div>innodb_locks_unsafe_for_binlog=off</div></td><td valign="top"><div>Share Next-Key</div></td><td valign="top"><div>Share Next-Key</div></td><td valign="top"><div>Share Next-Key</div></td><td valign="top"><div>Share Next-Key</div></td></tr><tr><td valign="top"><div>innodb_locks_unsafe_for_binlog=on</div></td><td valign="top"><div>None locks</div></td><td valign="top"><div>Consisten read/None lock</div></td><td valign="top"><div>Consisten read/None lock</div></td><td valign="top"><div>Share Next-Key</div></td></tr><tr><td rowspan="2"><div>create table ... Select ...</div><div>（指源表锁）</div></td><td valign="top"><div>innodb_locks_unsafe_for_binlog=off</div></td><td valign="top"><div>Share Next-Key</div></td><td valign="top"><div>Share Next-Key</div></td><td valign="top"><div>Share Next-Key</div></td><td valign="top"><div>Share Next-Key</div></td></tr><tr><td valign="top"><div>innodb_locks_unsafe_for_binlog=on</div></td><td valign="top"><div>None locks</div></td><td valign="top"><div>Consisten read/None lock</div></td><td valign="top"><div>Consisten read/None lock</div></td><td valign="top"><div>Share Next-Key</div></td></tr></tbody></table>



[MySQL数据库事务各隔离级别加锁情况--read committed && MVCC](http://www.imooc.com/article/17290?block_id=tuijian_wz)
