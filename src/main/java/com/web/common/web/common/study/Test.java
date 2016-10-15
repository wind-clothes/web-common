package com.web.common.web.common.study;

public class Test {

  public static void main(String[] args) {
    Greeting english = new Greeting();
    Greeting french = new FrenchGreeting();

    System.out.println(english.intro + "," + english.target());
    System.out.println(french.intro + "," + french.target());
    System.out.println(((FrenchGreeting) french).intro + "," + ((FrenchGreeting) french).target());
  }

  public static class Greeting {
    String intro = "Hello";

    String target() {
      return "world";
    }
  }
  public static class FrenchGreeting extends Greeting {
    String intro = "Bonjour";

    String target() {
      return "le monde";
    }
  }
}
