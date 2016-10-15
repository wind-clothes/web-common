package com.web.common.web.common.study.code;


public class Test {
  public static void main(String[] args) {   
      final CountDownLatch latch = new CountDownLatch(1);
       
      Thread thread = new Thread(){
          public void run() {
              try {
                  System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                 Thread.sleep(3000);
                 System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                 latch.countDown();
                 System.out.println("执行"+latch.getCount());
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
          };
      };
       
      thread.start();
//      new Thread(){
//          public void run() {
//              try {
//                  System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
//                  Thread.sleep(3000);
//                  System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
//                  latch.countDown();
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//          };
//      }.start();
       
      try {
          System.out.println("等待2个子线程执行完毕...");
         latch.await();
         System.out.println("2个子线程已经执行完毕");
         System.out.println("继续执行主线程");
         System.out.println(thread.isAlive());
     } catch (InterruptedException e) {
         e.printStackTrace();
     }
  }
}