package com.github.rpc.cluster;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询
 * Created by jinwei.li on 2017/7/4 0004.
 */
public class RoundLoadBalance implements LoadBalance {

    private ConcurrentMap<String, AtomicInteger> sequences = new ConcurrentHashMap<>();

    @Override
    public <T> T select(List<T> invokers, String seed) {
        Preconditions.checkNotNull(invokers);
        AtomicInteger sequence = sequences.get(seed);
        if (sequence == null) {
            sequences.putIfAbsent(seed, new AtomicInteger(0));
            sequence = sequences.get(seed);
        }
        int index = -1;
        int size = invokers.size();
        if(size == 0)
        {
            throw new IllegalStateException("provide not exists!");
        }
        if ((index = sequence.incrementAndGet()) < Integer.MAX_VALUE) {
            return invokers.get(index % size);
        } else {
            sequence.set(0);
            return select(invokers, seed);
        }
    }
}
