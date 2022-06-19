package com.jd.ee.janus.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jd.ee.common.utils.Assert;

/**
 * @author: xiongchengwei
 */
public class AsyncExecutor {

    /**
     * 异步计算，OneWay模式
     * 
     * @param runnable
     * @return void
     */
    public static void asyncRunnable(Runnable runnable) {
        Assert.notNull(runnable);
        CompletableFuture.runAsync(runnable);
    }

    public static void asyncRunnable(Runnable runnable, BiConsumer<Void, ? super Throwable> consumer,
            Executor executor) {
        Assert.notNull(runnable);
        Assert.notNull(executor);
        CompletableFuture.runAsync(runnable, executor).whenComplete(consumer);
    }

    /**
     * 异步计算，Callback回调模式
     * 
     * @param supplier
     * @param consumer
     * @return void
     */
    public static <T> CompletableFuture<T> asyncSupplierFuture(Supplier<T> supplier) {
        Assert.notNull(supplier);
        return CompletableFuture.supplyAsync(supplier);
    }

    /**
     * 异步计算，Callback回调模式
     * 
     * @param supplier
     * @param consumer
     * @return void
     */
    public static <T> void asyncSupplier(Supplier<T> supplier, BiConsumer<? super T, ? super Throwable> consumer) {
        Assert.notNull(supplier);
        Assert.notNull(consumer);
        CompletableFuture.supplyAsync(supplier).whenComplete(consumer);
    }

    /**
     * 异步计算，Callback回调模式
     * 
     * @param supplier
     * @param consumer
     * @return void
     */
    public static <T> void asyncSupplier(Supplier<T> supplier, BiConsumer<? super T, ? super Throwable> consumer,
            Executor executor) {
        Assert.notNull(supplier);
        Assert.notNull(consumer);
        Assert.notNull(executor);
        CompletableFuture.supplyAsync(supplier, executor).whenComplete(consumer);
    }

    /**
     * 异步计算，同步等待获取模式
     * 
     * @param supplier
     * @return AsyncResult
     */
    public static <T> Result<T> asyncSupplier(Supplier<T> supplier, Executor executor) {
        Assert.notNull(supplier);
        Assert.notNull(executor);
        return CompletableFuture.supplyAsync(() -> Result.success(supplier.get()), executor)
                .exceptionally((e) -> Result.fail(e)).join();
    }

    /**
     * 异步计算，同步等待获取模式
     * 
     * @param supplier
     * @return AsyncResult
     */
    public static <T> Result<T> asyncSupplier(Supplier<T> supplier) {
        Assert.notNull(supplier);
        return CompletableFuture.supplyAsync(() -> Result.success(supplier.get())).exceptionally((e) -> Result.fail(e))
                .join();
    }

    /**
     * <pre>
     * 批量异步执行任务
     * </pre>
     * 
     * @param suppliers
     * @return CompletableFuture<List<AsyncResult>>
     */
    public static <T> CompletableFuture<List<Result<T>>> batchAsyncSupplier(List<Supplier<T>> suppliers) {
        Assert.notNull(suppliers);
        List<CompletableFuture<Result<T>>> futures =
                suppliers.stream().map((supplier) -> CompletableFuture.supplyAsync(() -> Result.success(supplier.get()))
                        .exceptionally((e) -> Result.fail(e))).collect(Collectors.toList());
        return sequenceAsyncResult(futures);
    }

    /**
     * <pre>
     * 批量异步执行任务
     * </pre>
     * 
     * @param suppliers
     * @return CompletableFuture<List<AsyncResult>>
     */
    public static <T> CompletableFuture<List<Result<T>>> batchAsyncSupplier(List<Supplier<T>> suppliers,
            Executor executor) {
        Assert.notNull(suppliers);
        Assert.notNull(executor);
        List<CompletableFuture<Result<T>>> futures = suppliers.stream().map((supplier) -> CompletableFuture
                .supplyAsync(() -> Result.success(supplier.get()), executor).exceptionally((e) -> Result.fail(e)))
                .collect(Collectors.toList());
        return sequenceAsyncResult(futures);
    }


    /**
     * <pre>
     * 批量异步执行任务
     * </pre>
     * 
     * @param tasks
     * @return CompletableFuture<List<AsyncResult>>
     */
    public static <T> CompletableFuture<List<Result<T>>> batchAsync(List<AsyncTask<T>> tasks) {
        Assert.notNull(tasks);
        List<CompletableFuture<Result<T>>> futures =
                tasks.stream().map((task) -> CompletableFuture.supplyAsync(() -> Result.success(task.id(), task.get()))
                        .exceptionally((e) -> Result.fail(task.id(), e))).collect(Collectors.toList());
        return sequenceAsyncResult(futures);
    }

    /**
     * <pre>
     * 批量异步执行任务
     * </pre>
     * 
     * @param tasks
     * @return CompletableFuture<List<AsyncResult>>
     */
    public static <T> CompletableFuture<List<Result<T>>> batchAsync(List<AsyncTask<T>> tasks, Executor executor) {
        Assert.notNull(tasks);
        List<CompletableFuture<Result<T>>> futures = tasks.stream()
                .map((task) -> CompletableFuture.supplyAsync(() -> Result.success(task.id(), task.get()), executor)
                        .exceptionally((e) -> Result.fail(task.id(), e)))
                .collect(Collectors.toList());
        return sequenceAsyncResult(futures);
    }


    /**
     * <pre>
     * 批量异步执行任务,不带返回
     * </pre>
     * 
     * @param tasks
     * @return CompletableFuture<List<AsyncResult>>
     */
    public static <T> void batchSteamAsync(List<AsyncSteamTask<T>> tasks) {
        Assert.notNull(tasks);
        for (AsyncSteamTask<T> task : tasks) {
            CompletableFuture.supplyAsync(() -> task.get()).whenComplete((v, e) -> task.accept(v, e));
        }
    }

    /**
     * <pre>
     * 批量异步执行任务,不带返回
     * </pre>
     * 
     * @param tasks
     * @return CompletableFuture<List<AsyncResult>>
     */
    public static <T> void batchSteamAsync(List<AsyncSteamTask<T>> tasks, Executor executor) {
        Assert.notNull(tasks);
        for (AsyncSteamTask<T> task : tasks) {
            CompletableFuture.supplyAsync(() -> task.get(), executor).whenComplete((v, e) -> task.accept(v, e));
        }
    }

    /**
     * <pre>
     * 组合多个CompletableFuture为一个CompletableFuture,所有子任务全部完成，组合后的任务才会完成。带返回值，可直接get.
     * </pre>
     */
    public static <T> CompletableFuture<List<Result<T>>> sequenceAsyncResult(
            List<CompletableFuture<Result<T>>> futures) {
        Assert.notNull(futures);
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    /**
     * <pre>
     * 组合多个CompletableFuture为一个CompletableFuture,所有子任务全部完成，组合后的任务才会完成。带返回值，可直接get.
     * </pre>
     */
    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        Assert.notNull(futures);
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    /**
     * <pre>
     * Stream流式类型futures转换成一个CompletableFuture,所有子任务全部完成，组合后的任务才会完成。带返回值，可直接get.
     * </pre>
     */
    public static <T> CompletableFuture<List<T>> sequence(Stream<CompletableFuture<T>> futures) {
        Assert.notNull(futures);
        List<CompletableFuture<T>> futureList = futures.filter(f -> f != null).collect(Collectors.toList());
        return sequence(futureList);
    }

    public interface AsyncTask<R> extends Supplier<R> {
        String id();
    }

    public interface AsyncSteamTask<R> extends Supplier<R>, BiConsumer<R, Throwable> {
    }

    public static class Result<T> {
        protected boolean success = true;
        protected String id;
        protected T result;
        protected Throwable thx;

        protected Result(String id, boolean success, T result, Throwable thx) {
            super();
            this.id = id;
            this.success = success;
            this.result = result;
            this.thx = thx;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean getSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public T getResult() {
            return result;
        }

        public void setResult(T result) {
            this.result = result;
        }

        public Throwable getThx() {
            return thx;
        }

        public void setThx(Throwable thx) {
            this.thx = thx;
        }

        public static <E> Result<E> success(E result) {
            return new Result<E>(null, true, result, null);
        }

        public static <E> Result<E> success(String id, E result) {
            return new Result<E>(id, true, result, null);
        }

        public static <E> Result<E> fail(Throwable thx) {
            return new Result<E>(null, false, null, thx);
        }

        public static <E> Result<E> fail(String id, Throwable thx) {
            return new Result<E>(id, false, null, thx);
        }
    }
}
