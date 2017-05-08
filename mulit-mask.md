##mulit-mask
===========

* TaskBeanContainer:在启动的时候扫描所有的TaskService和TaskBean方法，并将其注入spring的容器中
，TaskBean对应的是一个个的Taskable的类型，其中有个worker的方法，用以实现方法的Invoke。

* TaskWrapper:实现了Runnable接口，在run方法中调用一个Taskable的worker方法，并把结果放进上下文TaskContext中，一个个的TaskBean的映射转化类，负责将Taskable映射成TaskWrapper暴露给调用方。

* WorkUnit：一组工作单元，将多个异步操作视为一个操作，其底层实现是通过ExecutorCompletionService来实现的，保证了任务的执行顺序，这里有一个waitForCompletion（loop等待所有的任务执行完成）。

```
+-------------+    +-----+    +-----+          +--------------+
|             |    |     |    |     |          |              |
|             |    |TaskWrapperTaskBean|       |              |
|             |    |     |    |Helper|         |              |
|             |    |     |    |     |          |              |
|             |    +-----+    +-----+          |              |
|             |    +----------------+          |              |
| TaskBeanContainer|WorkUnit一组工作单元, |      |TaskContext   |
|             |    |可视为一组原子操作 |          |              |
|             |    |                |          |              |
|             |    |                |          |              |
|             |    |                |          |              |
|             |    +----------------+          |              |
|             | +---------------------------+  |              |
|             | |TaskManager                |  |              |
+-------------+ +---------------------------+  +--------------+

```


