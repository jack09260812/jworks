package com.github.rpc.register;

import com.github.rpc.listener.NotifyChangeListener;
import org.I0Itec.zkclient.IZkChildListener;

/**
 * Created by jinwei.li on 2017/6/27 0027.
 */
public interface ServiceRegistry {
    /**
     * 注册节点
     * @param data
     * @return
     */
    boolean register(String data);

    /**
     * 取消节点
     * @param path
     * @return
     */
    boolean unregister(String path);

    /**
     * 监听节点变化
     * @param path
     * @param listener
     */
    void subscribe(String path,final NotifyChangeListener listener);
}
