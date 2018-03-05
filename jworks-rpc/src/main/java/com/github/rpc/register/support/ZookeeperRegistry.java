package com.github.rpc.register.support;

import com.github.rpc.listener.NotifyChangeListener;
import com.github.rpc.register.ServiceRegistry;
import com.google.common.base.Preconditions;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ZookeeperRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);
    private ZkClient zkClient;
    private String registryAddress;
    private String registryPath;

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int SESSION_TIMEOUT = 100000;
    private static final String REGISTRY_PATH = "/jworks-rpc";


    public ZookeeperRegistry() {
        this("127.0.0.1:2181", REGISTRY_PATH);
    }

    public ZookeeperRegistry(String registryAddress, String registryPath) {
        this.registryAddress = registryAddress;
        this.registryPath = registryPath;
        this.zkClient = new ZkClient(registryAddress, SESSION_TIMEOUT, CONNECT_TIMEOUT, new SerializableSerializer());
        logger.info("zk client:{}",zkClient);
    }

    public ZookeeperRegistry(String registryAddress) {
        this(registryAddress, REGISTRY_PATH);
    }

    /**
     * 创建节点
     * Ephemeral 临时节点：客户端会话失效或连接关闭后，该节点会被自动删除，且不能再临时节点下面创建子节点，否则报如下错：org.apache.zookeeper.KeeperException$NoChildrenForEphemeralsException；
     * EPHEMERAL_SEQUENTIAL 临时顺序节点：基本特性与临时节点一致，创建节点的过程中，zookeeper会在其名字后自动追加一个单调增长的数字后缀，作为新的节点名；
     * PERSISTENT 持久节点
     * PERSISTENT_SEQUENTIAL 持久顺序节点：基本特性与持久节点一致，创建节点的过程中，zookeeper会在其名字后自动追加一个单调增长的数字后缀，作为新的节点名；
     *
     * @param path
     * @return
     */
    public boolean register(String path) {
        try {
            Preconditions.checkNotNull(path, "zk注册节点路径不能为空");
            String fullPath = registryPath + path;
            String prefixPath = fullPath.substring(0, fullPath.lastIndexOf("/"));
            //如果注册的根节点不存在，注册根节点
            if (prefixPath != null && !zkClient.exists(prefixPath))
                zkClient.createPersistent(prefixPath, true);
            zkClient.createEphemeral(fullPath);
            logger.info("{}节点创建成功", path);
            return true;
        } catch (Exception e) {
            logger.error("创建节点失败", e);
        }
        return false;
    }

    @Override
    public boolean unregister(String path) {
        zkClient.delete(path);
        return true;
    }

    @Override
    public void subscribe(final String path, final NotifyChangeListener listener) {
        String fullPath = registryPath+"/" + path + "/providers";
        List<String> children = zkClient.getChildren(fullPath);
        listener.notifyChange(path, children);
        zkClient.subscribeChildChanges(fullPath, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                listener.notifyChange(path, list);
            }
        });
    }
}