package com.github.rpc.listener;


/**
 * Created by jinwei.li on 2017/7/5 0005.
 */
public interface NotifyChangeListener<K,V> {
    void notifyChange(K k, V v);
}
