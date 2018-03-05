package com.github.rpc;

import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by jinwei.li on 2017/7/17 0017.
 */
public class AQSTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Deque<RpcFuture> deque = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < 10; i++) {
            final RpcFuture future = new RpcFuture();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int time = new Random().nextInt(30000);
                        System.out.println(Thread.currentThread().getName() + "\t" + time);
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    future.done(Thread.currentThread().getName());
                }
            }).start();
            deque.push(future);
        }
        for (; ; ) {
            if (deque.size() == 0)
                break;
            RpcFuture future = deque.pollFirst();
            if (future.isDone())
                System.out.println(future.get());
            else {
                deque.offerLast(future);
            }

        }
    }

    static class RpcFuture implements Future<String> {
        private Syn syn;
        private String result;

        public RpcFuture() {
            this.syn = new Syn();
        }


        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return syn.isDone();
        }

        @Override
        public String get() throws InterruptedException, ExecutionException {
            syn.acquire(1);
            return result;
        }

        @Override
        public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

        public void done(String result) {
            this.result = result;
            syn.release(-1);
        }
    }

    static class Syn extends AbstractQueuedSynchronizer {

        private final int wait = 0;
        private final int finish = 1;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == finish;
        }

        @Override
        protected boolean tryRelease(int arg) {
            return compareAndSetState(wait, finish);
        }

        public boolean isDone() {
            return getState() == 1;
        }

    }
}
