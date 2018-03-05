package com.github.rpc;

import com.google.common.base.Joiner;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jinwei.li on 2017/7/6 0006.
 */
public class ZkTest {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(20);
        final Random random = new Random();
//        zkClient.createPersistent("/node");
        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ZkClient zkClient = new ZkClient("127.0.0.1:2181", 100, 30000);
                    final String path = zkClient.createEphemeralSequential("/node/test", null);
                    List<String> children = zkClient.getChildren("/node");
                    Collections.sort(children);
                    String temp = children.get(0);
                    final String threadName = Thread.currentThread().getName();
                    if (path.contains(temp))
                        System.out.println(threadName + "为主节点:"+path);
                    zkClient.subscribeChildChanges("/node", new IZkChildListener() {
                        @Override
                        public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                            Collections.sort(currentChilds);
                            String temp = currentChilds.get(0);
                            if (path.contains(temp))
                                System.out.println(threadName + "为主节点:"+temp+"---节点："+ Joiner.on(",").join(currentChilds));

                        }
                    });
                    latch.countDown();
                    try {
                        Thread.sleep(random.nextInt(1000 * 60));
                        zkClient.close();
                        System.out.println(Thread.currentThread().getName()+"结束");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        latch.await();
        System.out.println("节点初始化完毕");
        Thread.sleep(100000);
    }
}
