### jvm 分析工具和常用命令
jps(显示系统中所有Hotspot虚拟机进程
),jinfo(显示虚拟机的配置信息
),jstat(收集Hotspot虚拟机各方面运行数据
),jstack(显示虚拟机的线程栈信息
),jmap(用于生成虚拟机的内存快照信息
)

* jps:该命令用于列出正在运行的虚拟机进程，命令格式如下：
```
jps [-q] [-mlvV] [<hostid>]

options:
	-q  
	-m		（输出启动时传递给main函数的参数）
	-l       (输出主类全名)
	-v		 (输出虚拟机进程启动的jvm参数)
	-V		（）
-

```
* jstat:该命令用于监控各种运行状态信息，可以用来显示系统中类装载，垃圾回收，运行期编译状况等运行数据,命令格式是：
```
jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]] 
```
option参数可选值有：

```
class	类装载相关信息.

compiler	JIT编译器编译过的方法、耗时等.

gc	java堆信息和垃圾回收状况.

gccapacity	关注java堆各个区的最大和最小空间.

gccause	类似gcutil，额外输出导致上一次gc的原因.

gcnew	新生代gc状况.

gcnewcapacity	关注新生代gc使用的最大和最小空间.

gcold	老年代gc状况.

gcoldcapacity	关注老年代gc使用的最大和最小空间.

gcpermcapacity	关注持久代gc使用的最大和最小空间.

gcutil	关注已使用空间占总空间比例.

printcompilation	输出已经被JIT编译的方法.
```

vmid表示虚拟机唯一标识符，如果是本地虚拟机进程，与LVMID一致，通常为本地虚拟机进程号，interval表示查询间隔时间，count表示查询次数。如果省略interval和count参数，表示查询一次。 

* jstack：用于查看虚拟机当前的线程的状态信息，可以用该命令来查看分析死锁信息，命令格式入下：
```
jstack [ option ] pid 

Options:                                                                                                                                                        
    -F   （当请求不被响应时，强制输出线程堆栈）                                                                       
    -m   （混合模式，打印java和本地C++调用的堆栈信息）                                                                                                       
    -l   （除堆栈外，显示锁的附加信息）                                                                                            
    -h or -help to print this help message  
```
* jinfo:该命令能输出并修改运行时的java进程的运行参数，命令格式如下：
```
jinfo [option] <pid>                                                                                                                                        
        (to connect to running process)                                                                                                                                                                                                                                              
                                                                                                                                                                
where <option> is one of:                                                                                                                                       
    -flag <name>         (获取指定名称的运行参数信息)                                                                                    
    -flag [+|-]<name>   （设置运行参数是否有效）                                                                        
    -flag <name>=<value>（设置运行参数的值）                                                                         
    -flags              （获取所有参数的信息）                                                                                                                    
    -sysprops           （输出系统参数）                                                                                                    
    <no option>          (输出 -flags 和 -sysprops 的命令结果信息)                                                                                                           
    -h | -help           to print this help message  
```
* jmap:可以产生堆dump文件，查询堆和持久代的详细信息等,命令格式入下：
```
 jmap [option] <pid>                                                                                                                                         
        (to connect to running process)                                                                                                                                                                                                                                           
                                                                                                                                                                
where <option> is one of:                                                                                                                                       
    <none>               to print same info as Solaris pmap                                                                                                     
    -heap               （显示java堆的详细信息，包括垃圾回收期、堆配置和分代信息等）                                                                                                            
    -histo[:live]        (显示堆中对象的统计信息，包括类名称，对应的实例数量和总容量，live表示存活的数据的信息)                                                                                      
    -permstat           （统计持久代中各ClassLoader的统计信息。）                                                                                               
    -finalizerinfo       ()                                                                                 
    -dump:<dump-options> 生成堆dump文件，格式为: -dump:live,format=b,file=<filename>                                                                                                 
                         如: jmap -dump:live,format=b,file=heap.bin <pid>                              
    -F                   与-dump一起使用：<dump-options> <pid>或-histo
                          当<pid>不强制堆转储或直方图响应。 在这种模式下“live”子选项不支持。                                                                                                                         
    -h | -help           to print this help message
	-J<flag>             将<flag>直接传递给运行时系统                                                                                                            
```
