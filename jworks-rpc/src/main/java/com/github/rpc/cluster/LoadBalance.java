package com.github.rpc.cluster;

import java.util.List;

/**
 * Created by jinwei.li on 2017/7/4 0004.
 */
public interface LoadBalance {

    /**
     *
     * @param invokers
     * @param <T>
     * @return
     */
    <T> T select(List<T> invokers,String seed);
}
