package com.web.common.web.common.study.stream;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * <pre>
 * </pre>
 * @author: chengweixiong@uworks.cc
 * @date: 2016年6月1日 下午5:28:42
 */
public class DateDemo8 {

  public static void main(String[] args) {
    LocalDate date = LocalDate.now();
    System.out.println(date);
    LocalDateTime localDateTime = LocalDateTime.now();
    System.out.println(localDateTime);
    LocalTime time = LocalTime.now();
    System.out.println(time);
    Instant instant = Instant.now();
    System.out.println(instant);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    System.out.println(localDateTime.format(format));
  }
}
