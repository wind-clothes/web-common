package com.web.common.web.common.study.Nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <pre>
 * 线程模型是：单线程的。
 * 最原始的网络请求模型，开启一线程监听指定的端口，并监听连接成功的网络通道请求
 * 然后进行数据的读取，处理，写入返回，这些操作都是阻塞的
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2016年7月27日 下午2:30:01
 */
public class Server implements Runnable {

  private static final int PORT = 10001;

  @Override
  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(PORT);

      while (!Thread.interrupted()) {
        new Thread(new Handler(serverSocket.accept())).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class Handler implements Runnable {

    private static final int MAX_INPUT = 8096;

    final Socket socket;

    Handler(Socket s) {
      socket = s;
    }

    @Override
    public void run() {
      try {
        byte[] input = new byte[MAX_INPUT];
        socket.getInputStream().read(input);
        byte[] output = process(input);
        socket.getOutputStream().write(output);
      } catch (IOException ex) {
        /* ... */
        ex.printStackTrace();
      }
    }

    private byte[] process(byte[] cmd) {
      /* ... */
      return cmd;
    }

  }
}
