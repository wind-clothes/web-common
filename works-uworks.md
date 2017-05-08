```
反射

阻塞队列/非阻塞队列……线程的状态，线程调度常用的方法（wait/yield/join/notify）

既然说到了并发，最经典的不应该是这个么——写四个程序，分别造成四种不同原因的死锁。

内存缓存是哪种设计模式的实现-享元设计模式
66）在我 Java 程序中，我有三个 socket，我需要多少个线程来处理？
67）Java 中怎么创建 ByteBuffer？
68）Java 中，怎么读写 ByteBuffer ？
69）Java 采用的是大端还是小端？
70）ByteBuffer 中的字节序是什么？
71）Java 中，直接缓冲区与非直接缓冲器有什么区别？(答案)
72）Java 中的内存映射缓存区是什么？(answer答案)
73）socket 选项 TCP NO DELAY 是指什么？
74）TCP 协议与 UDP 协议有什么区别？(answer答案)
75）Java 中，ByteBuffer 与 StringBuffer有什么区别？(答案)
102）在没有使用临时变量的情况如何交换两个整数变量的值？(解决方案)


116）什么时候使用访问者模式？(答案)
访问者模式用于解决在类的继承层次上增加操作，但是不直接与之关联。这种模式采用双派发的形式来增加中间层。
123）抽象工厂模式和原型模式之间的区别？(答案)
124）什么时候使用享元模式？(答案)
享元模式通过共享对象来避免创建太多的对象。为了使用享元模式，你需要确保你的对象是不可变的，这样你才能安全的共享。JDK 中 String 池、Integer 池以及 Long 池都是很好的使用了享元模式的例子。

```
```
罚款：
资金流水记录

operationProjectId

Uw0rks123%
   

重构OfferCollectServiceImpl.buildOfferCollectServiceInfoList

@SpringApplicationConfiguration(classes = App.class)
@WebIntegrationTest
public class ReaderModuleJsonInitDBTest extends AbstractTestNGSpringContextTests {


  @Resource
  ModuleTemplateService moduleTemplateService;


  private final String ADMIN = "admin";
  private final Long ADMIN_ID = 1L;


  @Test
  public void init() throws IOException{
    moduleTemplateService.init();
  }


select new.s as size, user.id as id , user.`contact_name` as contactName,user.`username` AS username from t_reactor_user as user
 	LEFT JOIN (SELECT count(*) as s ,ptn.creator_id as creatorId from t_project_task_new as ptn where ptn.type = 2 and active = 1 GROUP BY ptn.creator_id) AS new ON new.creatorId = user.id 
 	where  user.status = 1 order by size desc;



select new.s as size, user.id as id , user.`contact_name` as contactName,user.`username` AS username from t_reactor_user as user
 	LEFT JOIN (SELECT count(*) as s , ptn.user_id as creatorId from t_project_user as ptn where ptn.status = 1 GROUP BY ptn.user_id) AS new ON new.creatorId = user.id 
 	where  user.status = 1 order by size desc;


select new.s as size, user.id as id , user.`contact_name` as contactName,user.`username` AS username from t_reactor_user as user
 	LEFT JOIN (SELECT ptn.responsible_id as creatorId,sum(period) as s from t_project_sub_task as ptn GROUP BY ptn.responsible_id)as new ON new.creatorId = user.id 
 	where  user.status = 1 order by size desc;

select new.s as size, user.id as id , user.contact_name as contactName from t_ops_user as user
 	LEFT JOIN (SELECT ptn.operator_id as creatorId,count(*) as s 
    from t_comment as ptn  where ptn.operator_type = 1 and ptn.type in (40,45)
    GROUP BY ptn.operator_id)as new ON new.creatorId = user.id 
 	 order by size desc;



select new.s as size, user.id as id , user.`contact_name` as contactName,user.`username` AS username from t_reactor_user as user
 	LEFT JOIN (SELECT ptn.operator_id as creatorId,COUNT(*) as s from t_project_log as ptn WHERE ptn.`operator_type` = 2 GROUP BY ptn.operator_id)as new ON new.creatorId = user.id 
 	where  user.status = 1 order by size desc;
```
```
v2.0.0补充sql：
ALTER TABLE `employer_feed_back` 
CHARACTER SET = utf8mb4 , COLLATE = utf8mb4_bin;
ALTER TABLE `employer_feed_back` 
CHANGE COLUMN `content` `content` VARCHAR(1024) NULL DEFAULT NULL COMMENT '意见反馈内容';
ALTER TABLE `job` 
ADD COLUMN `max_salary` INT(11) NOT NULL COMMENT '工资的上限，单位元' AFTER `salary`;
ALTER TABLE `employee_feed_back` 
CHARACTER SET = utf8mb4 , COLLATE = utf8mb4_bin;
ALTER TABLE `employee_feed_back` 
CHANGE COLUMN `content` `content` VARCHAR(1024) NULL DEFAULT NULL COMMENT '意见反馈内容';
update job set max_salary = salary where id > 0;
ALTER TABLE `gongkor_dev`.`job` 
CHANGE COLUMN `max_salary` `max_salary` INT(11) NULL COMMENT '工资的上限，单位元' ;
-- （线上环境已经执行）
ALTER TABLE `employee_works` 
  CHANGE COLUMN `job_start_date` `job_start_date` datetime NULL DEFAULT NULL comment '工作的开始日期',
  CHANGE COLUMN `job_stop_date` `job_stop_date` datetime NULL DEFAULT NULL comment '工作的结束日期';
-- （线上环境已经执行）
ALTER TABLE `employee_ audit_record`
RENAME TO  `employee_audit_record` ;
ALTER TABLE `employer_ audit_record`
RENAME TO  `employee_audit_record` ;
-- （线上环境已经执行）
ALTER TABLE `operator`
CHANGE COLUMN `operatorName` `operator_name` varchar(50) NOT NULL COMMENT '运营人员登录名';
--（线上环境已经执行）
ALTER TABLE `employer_marking_item` 
	DEFAULT CHARACTER SET=utf8mb4,
	COLLATE=utf8mb4_bin;
--（线上环境已经执行）
ALTER TABLE `employee_marking_item` 
	DEFAULT CHARACTER SET=utf8mb4,
	COLLATE=utf8mb4_bin;
--（线上环境已经执行）
ALTER TABLE `employee_marking_item` 
	MODIFY COLUMN `remark` `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注信息' AFTER `satisfaction`;
--（线上环境已经执行）
ALTER TABLE `employer_marking_item` 
	MODIFY COLUMN `remark` `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注信息' AFTER `create_time`;
ALTER TABLE `employee_works`
DROP COLUMN `check_id` ;
ALTER TABLE `employee_fellow_group`
DROP COLUMN `group_image` ;
ALTER TABLE `employee_attention_job` 
	ADD UNIQUE INDEX `unq_employeeid_jobid` (`employee_id` ASC, `job_id` ASC);
ALTER TABLE `employee_attention_employer` 
	ADD UNIQUE KEY `un_eeid_erid` (`employee_id` ASC, `employer_id` ASC);

http://192.168.10.31:7080

C端
B端：
OPS:

TIMER:
B端           http://employee.gongkor.com/
C端           http://employer.gongkor.com/
OPS          http://123.56.97.190:30001/
foodie      http://upload.gongkor.com


app1:
root
gongkor
wKaEZuQyGu$H
ZLz90Y&Rpu2L
101.200.123.101 (公)
10.44.14.88 (内)

app2:
root
gongkor
beAXwsJM#IP9
DmaLlQLvJoHp
123.57.30.64 (公)
10.172.228.26 (内)

employee_works:    job_start_date  null;job_stop_date null
employee_audit_record；employer_audit_record;
operator_name

ALTER TABLE `employer_marking_item` 
	DEFAULT CHARACTER SET=utf8mb4,
	COLLATE=utf8mb4_bin;

ALTER TABLE `employee_marking_item` 
	DEFAULT CHARACTER SET=utf8mb4,
	COLLATE=utf8mb4_bin;
	
ALTER TABLE `employee_marking_item` 
	MODIFY COLUMN `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注信息' AFTER `satisfaction`;

ALTER TABLE `employer_marking_item` 
	MODIFY COLUMN `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注信息' AFTER `create_time`;

employee_fellow_group 表中需要删除group_image

1、B端，邀附近，”取消邀请“的功能需求取消。 

2、求职者被商家录用后： 
1）C端，求职者在我的工作-已录用 工作列表中，可以点击“拒绝”按钮，如点击行为发生在工作开始时间24小时之外，无提示，如在24小时之内，提示“此操作将影响您的信用评价，降低找到更好工作的机会！”，确认或拒绝，还要C操作吗？B取消，影响评价 
2）B端，商家在求职者管理-已录用 工作列表中，点击“取消录用”的逻辑，与原型中描述一致。 

3、C端，关于邀请注册及红包： 
1）邀请码，根据在工客的注册用户账号生成唯一标识，可被多次使用； 
2）被邀请人在注册账号时输入此邀请码完成注册并进行个人实名认证，此邀请码对应的工客注册用户钱包中即被工客平台充值”1“元； 
3）被邀请人在工客平台成功完成首次工作（兼职工作以完成对商家评价作为完成首次工作标准，长期工作以完成在商家的”报到“作为完成首次工作的标准），此邀请码对应的工客注册用户钱包中即被工客平台充值”4“元； 
4）被邀请人成功注册后，即与此邀请码对应的工客注册用户互为好友关系。 

4、B端，24H急招及发布审核 
1）定义，发布岗位招聘时间距离岗位工作开始时间在24小时之内的招聘； 
2）发布审核，首个版本，对24H急招发布可不做审核；后续版本，策略如下： 
     a）首选机器人审核，即寻找开源的类舆情监控的软件包，自动审核涉”黄赌毒及反动暴力“的发布信息。 
     b）如机器人审核成本过高，当有新急招岗位发布时，自动推送短信给工客”审核人员“。 

5、C端，”找工作“的查询条件： 
1）首个版本取消”智能排序“； 
2）增加”星期“的条件周几时，做精准匹配 


5、C端，求职者发布求职 
1）功能和页面参考B端的发布管理，功能进行简化； 
2）求职者可以有多个求职发布； 
3）发布求职先选择工作类型（兼职/长期工作）； 
4）求职者在一次求职发布时可以选择多个岗位； 
5）工作日期与工作时间分开列示，日期可选择某日、某段日期范围以及长期； 
6）期望薪资，当选择长期工作时，单位为元/月，当选择兼职时，单位为元/日； 
7）自我评价，自动填充为上次填写的自动评价内容（如有）； 
7）必填字段：工作类型，其他均为选填 

6、C端，关于未注册用户能查看内容 
未注册用户只能看到工作详情页，详情页之中的内容包括其他求职责主页、企业主页等等其他内容时必须登录。 

7、关于视频文件的上传 
1）视频文件不能大于50M； 
2）视频文件上传帐户必须为工客认证实名账户； 
3）首个版本仅限商家可上传视频，求职者不能上传视频。
```