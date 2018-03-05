package com.github.shortlink;

/**
 * Created by lijinwei on 2017/4/13.
 */
public interface ShortLinkStorage<T extends ShortLinkGetter> {
    //TODO throws Exception，定义异常
    void save(T t);
    T get(String shortLink);
    void remove(T t);
    void cleanAll();
}
