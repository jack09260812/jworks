import com.github.rpc.Apple;
import com.github.rpc.RpcClient;
import com.github.rpc.RpcServer;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jinwei.li on 2017/7/17 0017.
 */
public class BasicTest {
    @Test
    public void testPlus() throws Throwable {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RpcServer server = RpcServer.getInstance();
                server.register(Apple.class.getName(), Apple.class);
            }
        }).start();
        Thread.sleep(1000);
        final CountDownLatch latch = new CountDownLatch(100);
        ExecutorService executor = Executors.newFixedThreadPool(10,new DefaultThreadFactory("rpc-plus-"));
        for(int i=0;i<100;i++){
            final int j = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Apple apple = null;
                    try {
                        apple = RpcClient.getInstance().proxy(Apple.class);
//                        System.out.println(apple.add(j));
                        latch.countDown();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
        }
        latch.await();
    }
}
