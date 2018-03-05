package com.github.utils.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具
 */
public class ThreadUtil {
    private static Logger logger = LoggerFactory.getLogger(ThreadUtil.class);


    /**
     * 新建一个线程池
     *
     * @param threadSize 同时执行的线程数大小
     * @return ExecutorService
     */
    public static ExecutorService newExecutor(int threadSize) {
        return Executors.newFixedThreadPool(threadSize);
    }

    /**
     * 获得一个新的线程池
     *
     * @return ExecutorService
     */
    public static ExecutorService newExecutor() {
        return Executors.newCachedThreadPool();
    }

    /**
     * 获得一个新的线程池，只有单个线程
     *
     * @return ExecutorService
     */
    public static ExecutorService newSingleExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    /**
     * 获得一个新的线程池<br>
     * 如果maximumPoolSize =》 corePoolSize，在没有新任务加入的情况下，多出的线程将最多保留60s
     *
     * @param corePoolSize    初始线程池大小
     * @param maximumPoolSize 最大线程池大小
     * @return {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor newExecutor(int corePoolSize, int maximumPoolSize) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, //
                60L, TimeUnit.SECONDS, //
                new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 获得一个新的线程池<br>
     * 传入阻塞系数，线程池的大小计算公式为：CPU可用核心数 / (1 - 阻塞因子)<br>
     * Blocking Coefficient(阻塞系数) = 阻塞时间／（阻塞时间+使用CPU的时间）<br>
     * 计算密集型任务的阻塞系数为0，而IO密集型任务的阻塞系数则接近于1。
     * <p>
     * see: http://blog.csdn.net/partner4java/article/details/9417663
     *
     * @param blockingCoefficient 阻塞系数，阻塞因子介于0~1之间的数，阻塞因子越大，线程池中的线程数越多。
     * @return {@link ThreadPoolExecutor}
     */
    public static ThreadPoolExecutor newExecutorByBlockingCoefficient(float blockingCoefficient) {
        if (blockingCoefficient > 1 || blockingCoefficient <= 0) {
            throw new IllegalArgumentException("[blockingCoefficient] must between 0 and 1, or equals 0.");
        }

        // 最佳的线程数 = CPU可用核心数 / (1 - 阻塞系数)
        int poolSize = (int) (Runtime.getRuntime().availableProcessors() / (1 - blockingCoefficient));

        return new ThreadPoolExecutor(poolSize, poolSize, //
                0L, TimeUnit.MILLISECONDS, //
                new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 直接在公共线程池中执行线程
     *
     * @param runnable 可运行对象
     */
    public static void execute(Runnable runnable) {
        GlobalThreadPool.execute(runnable);
    }

    /**
     * 执行异步方法
     *
     * @param runnable 需要执行的方法体
     * @param isDeamon 是否守护线程。守护线程会在主线程结束后自动结束
     * @return 执行的方法体
     */
    public static Runnable excAsync(final Runnable runnable, boolean isDeamon) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        thread.setDaemon(isDeamon);
        thread.start();

        return runnable;
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
     *
     * @param <T>  回调对象类型
     * @param task {@link Callable}
     * @return Future
     */
    public static <T> Future<T> execAsync(Callable<T> task) {
        return GlobalThreadPool.submit(task);
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
     *
     * @param runnable 可运行对象
     * @return {@link Future}
     */
    public static Future<?> execAsync(Runnable runnable) {
        return GlobalThreadPool.submit(runnable);
    }

    /**
     * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。<br>
     * 若未完成，则会阻塞
     *
     * @param <T> 回调对象类型
     * @return CompletionService
     */
    public static <T> CompletionService<T> newCompletionService() {
        return new ExecutorCompletionService<T>(GlobalThreadPool.getExecutor());
    }

    /**
     * 新建一个CompletionService，调用其submit方法可以异步执行多个任务，最后调用take方法按照完成的顺序获得其结果。<br>
     * 若未完成，则会阻塞
     *
     * @param <T>      回调对象类型
     * @param executor 执行器 {@link ExecutorService}
     * @return CompletionService
     */
    public static <T> CompletionService<T> newCompletionService(ExecutorService executor) {
        return new ExecutorCompletionService<T>(executor);
    }

    /**
     * 新建一个CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
     *
     * @param threadCount 线程数量
     * @return CountDownLatch
     */
    public static CountDownLatch newCountDownLatch(int threadCount) {
        return new CountDownLatch(threadCount);
    }

    /**
     * 挂起当前线程
     *
     * @param timeout  挂起的时长
     * @param timeUnit 时长单位
     * @return 被中断返回false，否则true
     */
    public static boolean sleep(Number timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout.longValue());
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * 挂起当前线程
     *
     * @param millis 挂起的毫秒数
     * @return 被中断返回false，否则true
     */
    public static boolean sleep(Number millis) {
        if (millis == null) {
            return true;
        }

        try {
            Thread.sleep(millis.longValue());
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * 考虑{@link Thread#sleep(long)}方法有可能时间不足给定毫秒数，此方法保证sleep时间不小于给定的毫秒数
     *
     * @param millis 给定的sleep时间
     * @return 被中断返回false，否则true
     * @see ThreadUtil#sleep(Number)
     */
    public static boolean safeSleep(Number millis) {
        long millisLong = millis.longValue();
        long done = 0;
        while (done < millisLong) {
            long before = System.currentTimeMillis();
            if (false == sleep(millisLong - done)) {
                return false;
            }
            long after = System.currentTimeMillis();
            done += (after - before);
        }
        return true;
    }

    /**
     * @return 获得堆栈列表
     */
    public static StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }

    /**
     * 获得堆栈项
     *
     * @param i 第几个堆栈项
     * @return 堆栈项
     */
    public static StackTraceElement getStackTraceElement(int i) {
        StackTraceElement[] stackTrace = getStackTrace();
        if (i < 0) {
            i += stackTrace.length;
        }
        return stackTrace[i];
    }

    /**
     * 创建本地线程对象
     *
     * @param <T>           持有对象类型
     * @param isInheritable 是否为子线程提供从父线程那里继承的值
     * @return 本地线程
     */
    public static <T> ThreadLocal<T> createThreadLocal(boolean isInheritable) {
        if (isInheritable) {
            return new InheritableThreadLocal<>();
        } else {
            return new ThreadLocal<>();
        }
    }

    /**
     * 结束线程，调用此方法后，线程将抛出 {@link InterruptedException}异常
     *
     * @param thread 线程
     * @param isJoin 是否等待结束
     */
    public static void interupt(Thread thread, boolean isJoin) {
        if (null != thread && false == thread.isInterrupted()) {
            thread.interrupt();
            if (isJoin) {
                waitForDie(thread);
            }
        }
    }

    /**
     * 等待线程结束. 调用 {@link Thread#join()} 并忽略 {@link InterruptedException}
     *
     * @param thread 线程
     */
    public static void waitForDie(Thread thread) {
        boolean dead = false;
        do {
            try {
                thread.join();
                dead = true;
            } catch (InterruptedException e) {
                //ignore
            }
        } while (!dead);
    }

    /**
     * 获取JVM中与当前线程同组的所有线程<br>
     *
     * @return 线程对象数组
     */
    public static Thread[] getThreads() {
        return getThreads(Thread.currentThread().getThreadGroup().getParent());
    }

    /**
     * 获取JVM中与当前线程同组的所有线程<br>
     * 使用数组二次拷贝方式，防止在线程列表获取过程中线程终止<br>
     *
     * @param group 线程组
     * @return 线程对象数组
     */
    public static Thread[] getThreads(ThreadGroup group) {
        final Thread[] slackList = new Thread[group.activeCount() * 2];
        final int actualSize = group.enumerate(slackList);
        final Thread[] result = new Thread[actualSize];
        System.arraycopy(slackList, 0, result, 0, actualSize);
        return result;
    }

    /**
     * 获取进程的主线程<br>
     *
     * @return 进程的主线程
     */
    public static Thread getMainThread() {
        for (Thread thread : getThreads()) {
            if (thread.getId() == 1) {
                return thread;
            }
        }
        return null;
    }

    static class GlobalThreadPool {
        private static ExecutorService executor;

        private GlobalThreadPool() {
        }

        static {
            init();
        }

        /**
         * 初始化全局线程池
         */
        synchronized public static void init() {
            if (null != executor) {
                executor.shutdownNow();
            }
            executor = Executors.newCachedThreadPool();
        }

        /**
         * 关闭公共线程池
         *
         * @param isNow 是否立即关闭而不等待正在执行的线程
         */
        synchronized public static void shutdown(boolean isNow) {
            if (null != executor) {
                if (isNow) {
                    executor.shutdownNow();
                } else {
                    executor.shutdown();
                }
            }
        }

        /**
         * 获得 {@link ExecutorService}
         *
         * @return {@link ExecutorService}
         */
        public static ExecutorService getExecutor() {
            return executor;
        }

        /**
         * 直接在公共线程池中执行线程
         *
         * @param runnable 可运行对象
         */
        public static void execute(Runnable runnable) {
            try {
                executor.execute(runnable);
            } catch (Exception e) {
                logger.error("Exception when running task!", e);
            }
        }

        /**
         * 执行有返回值的异步方法<br>
         * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
         *
         * @param <T>  执行的Task
         * @param task {@link Callable}
         * @return Future
         */
        public static <T> Future<T> submit(Callable<T> task) {
            return executor.submit(task);
        }

        /**
         * 执行有返回值的异步方法<br>
         * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
         *
         * @param runnable 可运行对象
         * @return {@link Future}
         */
        public static Future<?> submit(Runnable runnable) {
            return executor.submit(runnable);
        }
    }
}
