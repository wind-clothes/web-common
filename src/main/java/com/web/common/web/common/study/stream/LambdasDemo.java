package com.web.common.web.common.study.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * <pre>
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2016年5月30日 下午10:04:23
 */
public class LambdasDemo {
  private static final Name[] NAMES = new Name[] {new Name("Sally", "Smith")};

  private interface A {
    public int valueA(String s);
  }
  private interface B {
    public int valueB(String s);
  }

  public static void main(String[] args) {
    A a = String::length;
    B b = String::length;
  }

  public static void name() {
    List<Name> list = new ArrayList<>();
    for (Name name : NAMES) {
      list.add(name);
    }
    Predicate<Name> pred1 = name -> "Sally".equals(name.firstName);
    Predicate<Name> pred2 = name -> "Queue".equals(name.lastName);
    list.removeIf(pred1.or(pred2));
    printNames("Names filtered by predicate:", list.toArray(new Name[list.size()]));
  }

  private static void printNames(String string, Name[] array) {

  }
}


class Name {
  public final String firstName;
  public final String lastName;

  public Name(String first, String last) {
    firstName = first;
    lastName = last;
  }

  // only needed for chained comparator
  public String getFirstName() {
    return firstName;
  }

  // only needed for chained comparator
  public String getLastName() {
    return lastName;
  }

  // only needed for direct comparator (not for chained comparator)
  public int compareTo(Name other) {
    int diff = lastName.compareTo(other.lastName);
    if (diff == 0) {
      diff = firstName.compareTo(other.firstName);
    }
    return diff;
  }
}
