package com.web.common.web.common.study.Nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <pre>
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2016年7月27日 下午2:45:09
 */
public class Reactor implements Runnable {

  private final Selector selector;
  private final ServerSocketChannel serverSocketChannel;

  Reactor(int port) throws IOException {
    // 打开连接通道，并且监听指定的端口
    serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.bind(new InetSocketAddress(port));

    // Reactor
    selector = Selector.open();

    SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    key.attach(new Acceptor());// 注册callback对象
  }

  @Override
  public void run() {
    try {
      while (!Thread.interrupted()) {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = (SelectionKey) iterator.next();
          dispatch(key);
          iterator.remove();
        }
      }
    } catch (Exception e) {
    }
  }

  void dispatch(SelectionKey k) {
    Runnable r = (Runnable) (k.attachment()); // 调用之前注册的callback对象
    if (r != null)
      r.run();
  }

  public class Acceptor implements Runnable {

    @Override
    public void run() {
      try {
        SocketChannel c = serverSocketChannel.accept();
        if (c != null)
          new Handler(selector, c);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  final class Handler implements Runnable {
    private static final int MAXIN = 0;
    private static final int MAXOUT = 0;
    final SocketChannel socket;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(MAXIN);
    ByteBuffer output = ByteBuffer.allocate(MAXOUT);
    static final int READING = 0, SENDING = 1;
    int state = READING;

    Handler(Selector sel, SocketChannel c) throws IOException {
      socket = c;
      c.configureBlocking(false);
      // Optionally try first read now
      sk = socket.register(sel, SelectionKey.OP_READ);
      sk.attach(this); // 将Handler作为callback对象
      sk.interestOps(SelectionKey.OP_READ); // 第二步,接收Read事件
      sel.wakeup();
    }

    boolean inputIsComplete() {
      return false; /* ... */ }

    boolean outputIsComplete() {
      return false; /* ... */ }

    void process() { /* ... */ }

    public void run() {
      try {
        if (state == READING)
          read();
        else if (state == SENDING)
          send();
      } catch (IOException ex) {
        /* ... */ }
    }

    void read() throws IOException {
      socket.read(input);
      if (inputIsComplete()) {
        process();
        state = SENDING;
        // Normally also do first write now
        sk.interestOps(SelectionKey.OP_WRITE); // 第三步,接收write事件
      }
    }

    void send() throws IOException {
      socket.write(output);
      if (outputIsComplete())
        sk.cancel(); // write完就结束了, 关闭select key
    }
  }

  // 上面 的实现用Handler来同时处理Read和Write事件, 所以里面出现状态判断
  // 我们可以用State-Object pattern来更优雅的实现
//  class Handler { // ...
//    public void run() { // initial state is reader
//      socket.read(input);
//      if (inputIsComplete()) {
//        process();
//        sk.attach(new Sender()); // 状态迁移, Read后变成write, 用Sender作为新的callback对象
//        sk.interest(SelectionKey.OP_WRITE);
//        sk.selector().wakeup();
//      }
//    }
//
//    class Sender implements Runnable {
//      public void run() { // ...
//        socket.write(output);
//        if (outputIsComplete())
//          sk.cancel();
//      }
//    }
//  }
}
