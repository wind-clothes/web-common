package com.web.common.web.common.forkjoin;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;


/**
 * <pre>
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年3月27日 下午2:42:53
 */
public class ForkJoinDistance {

  public static void main(String[] args) {
    ForkJoinWorkerThreadFactory threadFactory = new ForkJoinWorkerThreadFactory() {
      @Override
      public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        return null;
      }
    };
    UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread t, Throwable e) {}
    };
    boolean asyncMode;
    int parallelism = Runtime.getRuntime().availableProcessors();
    ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism, threadFactory, handler, true);
  }

  private ForkJoinPool threadPool = new ForkJoinPool();

  private final String[] knownWords;

  private final int blockSize;

  public ForkJoinDistance(String[] words, int block) {
    knownWords = words;
    blockSize = block;
  }

  public DistancePair bestMatch(String target) {
    return threadPool.invoke(new DistanceTask(target, 0, knownWords.length, knownWords));
  }

  class DistanceTask extends RecursiveTask<DistancePair> {

    private static final long serialVersionUID = 1L;

    private final String compareText;
    private final int startOffset;
    private final int compareCount;
    private final String[] matchWords;

    public DistanceTask(String from, int offset, int count, String[] words) {
      compareText = from;
      startOffset = offset;
      compareCount = count;
      matchWords = words;
    }

    private int editDistance(int index, int[] v0, int[] v1) {
      return index;
    }

    @Override
    protected DistancePair compute() {
      if (compareCount > blockSize) {

        // split range in half and find best result from bests in each half of range
        int half = compareCount / 2;
        DistanceTask t1 = new DistanceTask(compareText, startOffset, half, matchWords);
        t1.fork();
        DistanceTask t2 =
            new DistanceTask(compareText, startOffset + half, compareCount - half, matchWords);
        DistancePair p2 = t2.compute();
        return DistancePair.best(p2, t1.join());
      }

      // directly compare distances for comparison words in range
      int[] v0 = new int[compareText.length() + 1];
      int[] v1 = new int[compareText.length() + 1];
      int bestIndex = -1;
      int bestDistance = Integer.MAX_VALUE;
      boolean single = false;
      for (int i = 0; i < compareCount; i++) {
        int distance = editDistance(i + startOffset, v0, v1);
        if (bestDistance > distance) {
          bestDistance = distance;
          bestIndex = i + startOffset;
          single = true;
        } else if (bestDistance == distance) {
          single = false;
        }
      }
      return single ? new DistancePair(bestDistance, knownWords[bestIndex])
          : new DistancePair(bestDistance);
    }
  }
}



class DistancePair {

  public DistancePair(int bestDistance, String string) {}

  public DistancePair(int bestDistance) {
    // TODO Auto-generated constructor stub
  }

  public static DistancePair best(DistancePair p2, DistancePair join) {
    return null;
  }

}
