## MySql

Oracle ASM
IBM HACMP
Oracle RAC
l  更新丢失（Lost Update）：当两个或多个事务选择同一行，然后基于最初选定的值更新该行时，由于每个事务都不知道其他事务的存在，就会发生丢失更新问题－－最后的更新覆盖了由其他事务所做的更新。例如，两个编辑人员制作了同一文档的电子副本。每个编辑人员独立地更改其副本，然后保存更改后的副本，这样就覆盖了原始文档。最后保存其更改副本的编辑人员覆盖另一个编辑人员所做的更改。如果在一个编辑人员完成并提交事务之前，另一个编辑人员不能访问同一文件，则可避免此问题。
l  脏读（Dirty Reads）：一个事务正在对一条记录做修改，在这个事务完成并提交前，这条记录的数据就处于不一致状态；这时，另一个事务也来读取同一条记录，如果不加控制，第二个事务读取了这些“脏”数据，并据此做进一步的处理，就会产生未提交的数据依赖关系。这种现象被形象地叫做"脏读"。
l  不可重复读（Non-Repeatable Reads）：一个事务在读取某些数据后的某个时间，再次读取以前读过的数据，却发现其读出的数据已经发生了改变、或某些记录已经被删除了！这种现象就叫做“不可重复读”。
l  幻读（Phantom Reads）：一个事务按相同的查询条件重新读取以前检索过的数据，却发现其他事务插入了满足其查询条件的新数据，这种现象就称为“幻读”。


读数据一致性及允许的并发副作用
隔离级别
读数据一致性
脏读
不可重复读
幻读
未提交读（Read uncommitted）
最低级别，只能保证不读取物理上损坏的数据
是
是
是
已提交度（Read committed）
语句级
否
是
是
可重复读（Repeatable read）
事务级
否
否
是
可序列化（Serializable）
最高级别，事务级
否
否
否


表20-16                                          InnoDB存储引擎中不同SQL在不同隔离级别下锁比较
隔离级别
        一致性读和锁
SQL
Read Uncommited
Read Commited
Repeatable Read
Serializable
SQL
条件
 	 	 	 
select
相等
None locks
Consisten read/None lock
Consisten read/None lock
Share locks
范围
None locks
Consisten read/None lock
Consisten read/None lock
Share Next-Key
update
相等
exclusive locks
exclusive locks
exclusive locks
Exclusive locks
范围
exclusive next-key
exclusive next-key
exclusive next-key
exclusive next-key
Insert
N/A
exclusive locks
exclusive locks
exclusive locks
exclusive locks
replace
无键冲突
exclusive locks
exclusive locks
exclusive locks
exclusive locks
键冲突
exclusive next-key
exclusive next-key
exclusive next-key
exclusive next-key
delete
相等
exclusive locks
exclusive locks
exclusive locks
exclusive locks
范围
exclusive next-key
exclusive next-key
exclusive next-key
exclusive next-key
Select ... from ... Lock in share mode
相等
Share locks
Share locks
Share locks
Share locks
范围
Share locks
Share locks
Share Next-Key
Share Next-Key
Select * from ... For update
相等
exclusive locks
exclusive locks
exclusive locks
exclusive locks
范围
exclusive locks
Share locks
exclusive next-key
exclusive next-key
Insert into ... Select ...
（指源表锁）
innodb_locks_unsafe_for_binlog=off
Share Next-Key
Share Next-Key
Share Next-Key
Share Next-Key
innodb_locks_unsafe_for_binlog=on
None locks
Consisten read/None lock
Consisten read/None lock
Share Next-Key
create table ... Select ...
（指源表锁）
innodb_locks_unsafe_for_binlog=off
Share Next-Key
Share Next-Key
Share Next-Key
Share Next-Key
innodb_locks_unsafe_for_binlog=on
None locks
Consisten read/None lock
Consisten read/None lock
Share Next-Key
