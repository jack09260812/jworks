package com.github.rpc.remote.support;

import com.github.rpc.context.RpcContext;
import com.github.rpc.model.RpcFuture;
import com.github.rpc.model.RpcRequest;
import com.github.rpc.model.RpcResponse;
import com.github.rpc.proxy.ProxyHandler;
import com.github.rpc.remote.RemoteServer;
import com.github.rpc.remote.RpcDecoder;
import com.github.rpc.remote.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty的流处理模型：
 * <p>
 * Boss线程（一个服务器端口对于一个）---接收到客户端连接---生成Channel---交给Work线程池（多个Work线程）来处理。
 * <p>
 * 具体的Work线程---读完已接收的数据到ChannelBuffer---触发ChannelPipeline中的ChannelHandler链来处理业务逻辑。
 * <p>
 * 注意：执行ChannelHandler链的整个过程是同步的，如果业务逻辑的耗时较长，会将导致Work线程长时间被占用得不到释放，从而影响了整个服务器的并发处理能力。
 * Created by jinwei.li on 2017/6/27 0027.
 */
public class NettyRemoteServer implements RemoteServer {

    private Logger logger = LoggerFactory.getLogger(getClass());
    //线程池大小
    private static final int THREAD_NUMS = 32;
    private static final long MAX_CHANNEL_MEMORY_SIZE = 10485760;
    private static final long MAX_TOTAL_MEMORY_SIZE = 104857600;

    private String host;
    private int port;
    private ProxyHandler proxyHandler;
    private Object obj = new Object();

    public NettyRemoteServer(RpcContext context) {
        proxyHandler = context.getProxyHandler();
    }

    public NettyRemoteServer(RpcContext context, String host, int port) {
        proxyHandler = context.getProxyHandler();
        this.host = host;
        this.port = port;
    }

    /**
     * 启动server服务
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        final EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(20);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))//防止tcp粘包
                                    .addLast(new RpcDecoder(RpcRequest.class)) // 将 RPC 请求进行解码（为了处理请求）
                                    .addLast(new RpcEncoder(RpcResponse.class)) // 将 RPC 响应进行编码（为了返回响应）
                                    .addLast(businessGroup, new NettyResponseHandler(proxyHandler)); // 使用businessGroup处理 RPC 请求，防止工作线程影响正常channel线程
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String host = this.host;
            int port = this.port;
            // 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.debug("server started on port {}", port);
            // 应用程序会一直等待，直到channel关闭
            future.channel().closeFuture().sync();
        } finally {
            //关闭EventLoopGroup，释放掉所有资源包括创建的线程
            workerGroup.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        }
    }

    @Override
    //todo 客户端连接池实现长连接 http://netty.io/wiki/user-guide-for-4.x.html#wiki-h3-10
    public RpcFuture send(RpcRequest request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))//防止tcp粘包
                                    .addLast(new RpcEncoder(RpcRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                                    .addLast(new RpcDecoder(RpcResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                                    .addLast(new NettyRequestHandler(proxyHandler)); //发送 RPC 请求
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect(request.getHost(), request.getPort()).sync();
            future.channel().writeAndFlush(request).sync();

            RpcFuture rpcFuture = new RpcFuture(request);
            RpcContext.rpcFutures.put(request.getRequestId(), rpcFuture);
            future.channel().closeFuture().sync();
            return rpcFuture;
        } finally {
            group.shutdownGracefully();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
