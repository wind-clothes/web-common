package com.web.common.web.common.algorithm;

/**
 * <pre>
 *  深度优先搜索和广度优先搜索，查找
 * </pre>
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2016年5月20日 下午11:54:00
 */
public class MouseMaze {
  private int startI, startJ; // 入口
  private int endI, endJ; // 出口
  private boolean success = false;

  public static void main(String[] args) { // 迷宫2表示墙 ，@表示出口和入口，0表示可走
    int[][] maze = {{2, 2, 2, 2, 2, 2, 2}, {1, 0, 0, 0, 0, 0, 2}, {2, 0, 2, 0, 2, 0, 2},
        {2, 0, 0, 0, 0, 2, 2}, {2, 2, 0, 2, 0, 2, 2}, {2, 0, 0, 0, 0, 0, 1}, {2, 2, 2, 2, 2, 2, 2}};

    System.out.println("显示迷宫：");
    for (int i = 0; i < maze.length; i++) {
      for (int j = 0; j < maze[0].length; j++) {
        if (maze[i][j] == 2) {
          System.out.print("██");
        } else if (maze[i][j] == 1) {
          System.out.print(" @");
        } else {
          System.out.print("  ");
        }
      }
      System.out.println();
    }

    MouseMaze mouse = new MouseMaze();
    mouse.setStart(1, 1);
    mouse.setEnd(5, 5);
    // No.2
    // 开始写代码，在这里判断小老鼠是否走出了迷宫,并且打印出图形。
    if (!mouse.go(maze)) {
      System.out.println("\n没有找到出口！");
    } else {
      System.out.println("\n找到出口！");
      for (int i = 0; i < maze.length; i++) {
        for (int j = 0; j < maze[0].length; j++) {
          if (maze[i][j] == 2)
            System.out.print("■");
          else if (maze[i][j] == 1)
            System.out.print("&");
          else
            System.out.print("  ");
        }
        System.out.println();
      }
    }
    // end_code
  }

  public void setStart(int i, int j) {
    this.startI = i;
    this.startJ = j;
  }

  public void setEnd(int i, int j) {
    this.endI = i;
    this.endJ = j;
  }

  public boolean go(int[][] maze) {
    return visit(maze, startI, startJ);
  }

  private boolean visit(int[][] maze, int i, int j) {
    maze[i][j] = 1;

    if (i == endI && j == endJ)
      success = true;

    if (!success && maze[i][j + 1] == 0)
      visit(maze, i, j + 1);
    if (!success && maze[i + 1][j] == 0)
      visit(maze, i + 1, j);
    if (!success && maze[i][j - 1] == 0)
      visit(maze, i, j - 1);
    if (!success && maze[i - 1][j] == 0)
      visit(maze, i - 1, j);

    if (!success)
      maze[i][j] = 0;

    return success;
  }

}
