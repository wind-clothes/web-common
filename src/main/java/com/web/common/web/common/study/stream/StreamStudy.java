package com.web.common.web.common.study.stream;

import java.util.stream.Stream;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2016年5月27日 下午3:54:10
 */
public class StreamStudy {

  public static void main(String[] args) {
    String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);

    double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);

  }
}
