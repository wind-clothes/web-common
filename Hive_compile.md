## Hive Sql的编译解析过程

http://img.blog.csdn.net/20170614225653740?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjkyMjgzOA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast

* Antlr定义SQL的语法规则，完成SQL词法，语法解析，将SQL转化为抽象语法树AST Tree，hive定义了词法和语法的编译规则文件，0.11版本将Hive.g拆成了5个文件，词法规则HiveLexer.g和语法规则的4个文件SelectClauseParser.gFromClauseParser.g，IdentifiersParser.g，HiveParser.g
* 遍历AST Tree，抽象出查询的基本组成单元QueryBlock，QueryBlock是一条SQL最基本的组成单元，包括三个部分：输入源，计算过程，输出.
    
    AST Tree生成QueryBlock
    * ASTTree生成QueryBlock的过程是一个递归的过程，先序遍历ASTTree，遇到不同的Token节点，保存到相应的属性中，主要包含以下几个过程
    * TOK_QUERY => 创建QB对象，循环递归子节点
    * TOK_FROM => 将表名语法部分保存到QB对象的aliasToTabs等属性中
    * TOK_INSERT => 循环递归子节点
    * TOK_DESTINATION => 将输出目标的语法部分保存在QBParseInfo对象的nameToDest属性中
    * TOK_SELECT => 分别将查询表达式的语法部分保存在destToSelExpr、destToAggregationExprs、destToDistinctFuncExprs三个属性中
    * TOK_WHERE => 将Where部分的语法保存在QBParseInfo对象的destToWhereExpr属性中

* 遍历QueryBlock，翻译为执行操作树OperatorTree,操作树中的每一个操作是一个个的MapReduce的任务.
    
    基本的操作符包括TableScanOperator，SelectOperator，FilterOperator，JoinOperator，GroupByOperator，ReduceSinkOperator

    Operator类的主要属性和方法如下
    * RowSchema表示Operator的输出字段
    * InputObjInspector outputObjInspector解析输入和输出字段
    * processOp接收父Operator传递的数据，forward将处理好的数据传递给子Operator处理
    * Hive每一行数据经过一个Operator处理之后，会对字段重新编号，colExprMap记录每个表达式经过当前Operator处理前后的名称对应关系，在下一个阶段逻辑优化阶段用来回溯字段名
    * 由于Hive的MapReduce程序是一个动态的程序，即不确定一个MapReduce Job会进行什么运算，可能是Join，也可能是GroupBy，所以Operator将所有运行时需要的参数保存在OperatorDesc中，OperatorDesc在提交任务前序列化到HDFS上，在MapReduce任务执行前从HDFS读取并反序列化。Map阶段OperatorTree在HDFS上的位置在Job.getConf(“hive.exec.plan”) + “/map.xml”

    QueryBlock生成Operator Tree
    QueryBlock生成Operator Tree就是遍历上一个过程中生成的QB和QBParseInfo对象的保存语法的属性，包含如下几个步骤：
    * QB#aliasToSubq => 有子查询，递归调用
    * QB#aliasToTabs => TableScanOperator
    * QBParseInfo#joinExpr => QBJoinTree => ReduceSinkOperator + JoinOperator
    * QBParseInfo#destToWhereExpr => FilterOperator
    * QBParseInfo#destToGroupby => ReduceSinkOperator + GroupByOperator
    * QBParseInfo#destToOrderby => ReduceSinkOperator + ExtractOperator
    * 由于Join/GroupBy/OrderBy均需要在Reduce阶段完成，所以在生成相应操作的Operator之前都会先生成一个ReduceSinkOperator，将字段组合并序列化为Reduce Key/value, Partition Key

* 逻辑层优化器进行OperatorTree变换，合并不必要的ReduceSinkOperator，减少shuffle数据量.
    大部分逻辑层优化器通过变换OperatorTree，合并操作符，达到减少MapReduce Job，减少shuffle数据量的目的。

    <table class="confluenceTable tablesorter"><thead><tr class="sortableHeader"><th class="confluenceTh sortableHeader tablesorter-headerSortUp" data-column="0"><div class="tablesorter-header-inner"><p class="p2"><span class="s1">名称</span></p></div></th><th class="confluenceTh sortableHeader" data-column="1"><div class="tablesorter-header-inner"><p class="p2"><span class="s1">作用</span></p></div></th></tr></thead><tbody class=""><tr><td class="confluenceTd"><p class="p3"><span class="s1">②&nbsp;SimpleFetchOptimizer</span></p></td><td class="confluenceTd"><p class="p4"><span class="s1">优化没有</span><span class="s3">GroupBy</span><span class="s1">表达式的聚合查询</span></p></td></tr><tr><td class="confluenceTd"><p class="p3"><span class="s1">②&nbsp;MapJoinProcessor</span></p></td><td class="confluenceTd"><p class="p3"><span class="s1">MapJoin</span><span class="s2">，需要SQL中提供</span><span class="s1">hint，0.11版本已不用</span></p></td></tr><tr><td class="confluenceTd"><p class="p3"><span class="s1">②&nbsp;BucketMapJoinOptimizer</span></p></td><td class="confluenceTd"><p class="p3"><span class="s1">BucketMapJoin</span></p></td></tr><tr><td class="confluenceTd"><p class="p3"><span class="s1">② GroupByOptimizer</span></p></td><td class="confluenceTd"><p class="p3"><span class="s1">Map</span><span class="s2">端聚合</span></p></td></tr><tr><td class="confluenceTd"><p class="p3"><span class="s1"><span>① </span>ReduceSinkDeDuplication</span></p></td><td class="confluenceTd"><p class="p3"><span class="s2">合并线性的</span><span class="s1">OperatorTree</span><span class="s2">中</span><span class="s1">partition/sort key</span><span class="s2">相同的</span><span class="s1">reduce</span></p></td></tr><tr><td class="confluenceTd"><p class="p3"><span class="s1">① PredicatePushDown</span></p></td><td class="confluenceTd"><p class="p4"><span class="s1">谓词前置</span></p></td></tr><tr><td class="confluenceTd"><p class="p3"><span class="s1"><span>① </span>CorrelationOptimizer</span></p></td><td class="confluenceTd"><p class="p4"><span class="s1">利用查询中的相关性，合并有相关性的</span><span class="s3">Job</span><span class="s1">，</span><span class="s3">HIVE-2206</span></p></td></tr><tr><td class="confluenceTd"><p class="p3"><span class="s1">ColumnPruner</span></p></td><td class="confluenceTd"><p class="p4"><span class="s1">字段剪枝</span></p></td></tr></tbody></table>

    表格中①的优化器均是一个Job干尽可能多的事情/合并。②的都是减少shuffle数据量，甚至不做Reduce。CorrelationOptimizer优化器非常复杂，都能利用查询中的相关性，合并有相关性的Job，参考 Hive Correlation Optimizer,对于样例SQL，有两个优化器对其进行优化。下面分别介绍这两个优化器的作用，并补充一个优化器ReduceSinkDeDuplication的作用

* 遍历OperatorTree，翻译为MapReduce任务.
    OperatorTree转化为MapReduce Job的过程分为下面几个阶段

    * 对输出表生成MoveTask
    * 从OperatorTree的其中一个根节点向下深度优先遍历
    * ReduceSinkOperator标示Map/Reduce的界限，多个Job间的界限
    * 遍历其他根节点，遇过碰到JoinOperator合并MapReduceTask
    * 生成StatTask更新元数据
    * 剪断Map与Reduce间的Operator的关系
* 物理层优化器进行MapReduce任务的变换，生成最终的执行计划.


https://tech.meituan.com/hive-sql-to-mapreduce.html
