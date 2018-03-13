package com.web.common.web.common.study.stream;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * <pre>
 * </pre>
 * @author: chengweixiong@uworks.cc
 * @date: 2016年6月1日 下午6:35:42
 */
public class CompletableFutureDemo {

  public static void main(String[] args) {
    CompletableFuture completableFuture = new CompletableFutureDemo().ask();
    try {
     System.out.println(completableFuture.get());;
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  public CompletableFuture<String> ask() {
    final CompletableFuture<String> future = new CompletableFuture<>();
    return future;
}
}
