## Python Study

=============================

### 注意

- 内置函数：
int(),range(),list=>[],tuple=>(),abs(),cmp()比较，float，str,unicode,bool,enumerate()函数可以把一个list变成索引-元素对，

isinstance('abc', Iterable) # str是否可迭代



- 必选参数,∂,可变参数(*param),关键字参数**param）

## 高级特性

   - 切片（Slice）操作符：
   如：L[1:3]正数取值，L[-2,-1]倒数取值,L[:]复制集合，字符串也支持切片'ABCDEFG'[:3] =》'ABC' 'ABCDEFG'[::2] =》 'ACEG'

   - 列表生成式：
   L = ['Hello', 'World', 18, 'Apple', None]

   [s.lower() if isinstance(s, str) else s for s in L]

   - 在Python中，这种一边循环一边计算的机制，称为生成器（Generator）:

   g = (x * x for x in range(10))  将表达式中的[]改为()

   def fib(max):
    n, a, b = 0, 0, 1
    while n < max:
        yield b     #这里，最难理解的就是generator和函数的执行流程不一样。函数是顺序执行，遇到return语句或者最后一行函数语句就返回。而变成generator的函数，在每次调用next()的时候执行，遇到yield语句返回，再次执行时从上次返回的yield语句处继续执行。
        a, b = b, a + b
        n = n + 1

## 函数式编程
    - 高阶函数

        - 变量可以指向函数：f = abs

        - 函数名也是变量：那么函数名是什么呢？函数名其实就是指向函数的变量！对于abs()这个函数，完全可以把函数名abs看成变量，它指向一个可以计算绝对值的函数！

        - 传入函数:既然变量可以指向函数，函数的参数能接收变量，那么一个函数就可以接收另一个函数作为参数，这种函数就称之为高阶函数。
        def add(x, y, f):
            return f(x) + f(y)

        add(-5, 6, abs)

        - map/reduce:map()/reduce()
        - filter:filter()
        - sorted:sorted()
    - 返回函数：
        - 函数作为返回值：
        - 闭包：
    - 匿名函数：
    - 装饰器：
    - 偏函数：functools.partial就是帮助我们创建一个偏函数的，不需要我们自己定义int2()，可以直接使用下面的代码创建一个新的函数int2
    >>> import functools
    >>> int2 = functools.partial(int, base=2)
    >>> int2('1000000')
        64
    >>> int2('1010101')
        85

## 面向对象编程
    - 类和实例：

    - 访问控制：

    - 集成与多态：

    - 获取对象信息：

    - 实例属性和类属性

## 面向对象高级编程
    - 使用_slots_:

    - 使用@property

    - 多重继承

    - 定制类

    - 使用枚举类

    - 使用元类

## 错误，调试和测试
    - 错误处理

    - 调试

    - 单元测试

    - 文档测试

## IO编程
    - 文件读写

    - stringIO和BytesIO

    - 操作文件和目录

    - 序列化

## 网络编程

## 数据库连接

## 电子邮件
    - SMTP发送邮件

    - POP3收取邮件

## 进程和线程

    - 多进程

    - 多线程

    - ThreadLocal：
        在多线程环境下，每个线程都有自己的数据。一个线程使用自己的局部变量比使用全局变量好，因为局部变量只有线程自己能看见，不会影响其他线程，而全局变量的修改必须加锁。

    - 分布式进程

## 异步IO
    - 协程：
        在学习异步IO模型前，我们先来了解协程。

        协程，又称微线程，纤程。英文名Coroutine。

        协程的概念很早就提出来了，但直到最近几年才在某些语言（如Lua）中得到广泛应用。

        子程序，或者称为函数，在所有语言中都是层级调用，比如A调用B，B在执行过程中又调用了C，C执行完毕返回，B执行完毕返回，最后是A执行完毕。

        所以子程序调用是通过栈实现的，一个线程就是执行一个子程序。

        子程序调用总是一个入口，一次返回，调用顺序是明确的。而协程的调用和子程序不同。

        协程看上去也是子程序，但执行过程中，在子程序内部可中断，然后转而执行别的子程序，在适当的时候再返回来接着执行。

    - asyncio：
        asyncio是Python 3.4版本引入的标准库，直接内置了对异步IO的支持。

        asyncio的编程模型就是一个消息循环。我们从asyncio模块中直接获取一个EventLoop的引用，然后把需要执行的协程扔到EventLoop中执行，就实现了异步IO。

    - async/await：
        用asyncio提供的@asyncio.coroutine可以把一个generator标记为coroutine类型，然后在coroutine内部用yield from调用另一个coroutine实现异步操作。
        
        为了简化并更好地标识异步IO，从Python 3.5开始引入了新的语法async和await，可以让coroutine的代码更简洁易读。

        请注意，async和await是针对coroutine的新语法，要使用新的语法，只需要做两步简单的替换：

            - 把@asyncio.coroutine替换为async；
            - 把yield from替换为await。

    - aiohttp：
        asyncio可以实现单线程并发IO操作。如果仅用在客户端，发挥的威力不大。如果把asyncio用在服务器端，例如Web服务器，由于HTTP连接就是IO操作，因此可以用单线程+coroutine实现多用户的高并发支持。asyncio实现了TCP、UDP、SSL等协议，aiohttp则是基于asyncio实现的HTTP框架。







## 协程

协程，又称微线程，纤程。英文名Coroutine。

协程的概念很早就提出来了，但直到最近几年才在某些语言（如Lua）中得到广泛应用。

子程序，或者称为函数，在所有语言中都是层级调用，比如A调用B，B在执行过程中又调用了C，C执行完毕返回，B执行完毕返回，最后是A执行完毕。

所以子程序调用是通过栈实现的，一个线程就是执行一个子程序。

子程序调用总是一个入口，一次返回，调用顺序是明确的。而协程的调用和子程序不同。

协程看上去也是子程序，但执行过程中，在子程序内部可中断，然后转而执行别的子程序，在适当的时候再返回来接着执行。

注意，在一个子程序中中断，去执行其他子程序，不是函数调用，有点类似CPU的中断。比如子程序A、B：

def A():
    print '1'
    print '2'
    print '3'

def B():
    print 'x'
    print 'y'
    print 'z'
假设由协程执行，在执行A的过程中，可以随时中断，去执行B，B也可能在执行过程中中断再去执行A，结果可能是：

1
2
x
y
3
z
但是在A中是没有调用B的，所以协程的调用比函数调用理解起来要难一些。

看起来A、B的执行有点像多线程，但协程的特点在于是一个线程执行，那和多线程比，协程有何优势？

最大的优势就是协程极高的执行效率。因为子程序切换不是线程切换，而是由程序自身控制，因此，没有线程切换的开销，和多线程比，线程数量越多，协程的性能优势就越明显。

第二大优势就是不需要多线程的锁机制，因为只有一个线程，也不存在同时写变量冲突，在协程中控制共享资源不加锁，只需要判断状态就好了，所以执行效率比多线程高很多。

因为协程是一个线程执行，那怎么利用多核CPU呢？最简单的方法是多进程+协程，既充分利用多核，又充分发挥协程的高效率，可获得极高的性能。

Python对协程的支持还非常有限，用在generator中的yield可以一定程度上实现协程。虽然支持不完全，但已经可以发挥相当大的威力了。

来看例子：

传统的生产者-消费者模型是一个线程写消息，一个线程取消息，通过锁机制控制队列和等待，但一不小心就可能死锁。

如果改用协程，生产者生产消息后，直接通过yield跳转到消费者开始执行，待消费者执行完毕后，切换回生产者继续生产，效率极高：

import time

def consumer():
    r = ''
    while True:
        n = yield r
        if not n:
            return
        print('[CONSUMER] Consuming %s...' % n)
        time.sleep(1)
        r = '200 OK'

def produce(c):
    c.next()
    n = 0
    while n < 5:
        n = n + 1
        print('[PRODUCER] Producing %s...' % n)
        r = c.send(n)
        print('[PRODUCER] Consumer return: %s' % r)
    c.close()

if __name__=='__main__':
    c = consumer()
    produce(c)
执行结果：

[PRODUCER] Producing 1...
[CONSUMER] Consuming 1...
[PRODUCER] Consumer return: 200 OK
[PRODUCER] Producing 2...
[CONSUMER] Consuming 2...
[PRODUCER] Consumer return: 200 OK
[PRODUCER] Producing 3...
[CONSUMER] Consuming 3...
[PRODUCER] Consumer return: 200 OK
[PRODUCER] Producing 4...
[CONSUMER] Consuming 4...
[PRODUCER] Consumer return: 200 OK
[PRODUCER] Producing 5...
[CONSUMER] Consuming 5...
[PRODUCER] Consumer return: 200 OK
注意到consumer函数是一个generator（生成器），把一个consumer传入produce后：

首先调用c.next()启动生成器；

然后，一旦生产了东西，通过c.send(n)切换到consumer执行；

consumer通过yield拿到消息，处理，又通过yield把结果传回；

produce拿到consumer处理的结果，继续生产下一条消息；

produce决定不生产了，通过c.close()关闭consumer，整个过程结束。

整个流程无锁，由一个线程执行，produce和consumer协作完成任务，所以称为“协程”，而非线程的抢占式多任务。

最后套用Donald Knuth的一句话总结协程的特点：

“子程序就是协程的一种特例。”


