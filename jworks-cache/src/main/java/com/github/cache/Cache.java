package com.github.cache;

/**
 * Created by jinwei on 17-8-25.
 */
public interface Cache<K extends CacheKey,V extends CacheObject> {
    V get(K k);

}
