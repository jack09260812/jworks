package com.github.cache.utils;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * Created by jinwei on 17-8-25.
 */
public class EhcacheUtil {
    public static void main(String[] args) {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                // 通过CacheManagerBuilder 的静态方法newCacheManagerBuilder 实例化CacheManager对象
                .withCache(
                        "preConfigured",
                        //通过CacheConfigurationBuilder 的静态方法newCacheManagerBuilder 来创建缓存Cache 的配置CacheConfiguration ，
                        //然后通过CacheManager的静态方法withCache 注册配置
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.heap(10))).build();
        cacheManager.init();

        //通过CacheManager的静态方法getCache获取Cache 对象
        Cache<Long, String> preConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);

        //通过CacheManager的方法createCache创建另一个不同配置Cache对象
        Cache<Long, String> myCache = cacheManager.createCache("myCache", CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)).build());

        //拿到Cache 对象后，可调用put设置缓存值key-value，可调用get通过可以获取value
        //通过CacheManager的静态方法removeCache获取删除Cache对象
        myCache.put(1L, "da one!");
        String value = myCache.get(1L);
        System.out.println("value=" + value);
        cacheManager.removeCache("preConfigured");
        //最后可通过CacheManager的静态方法close关闭CacheManager
        cacheManager.close();
    }
}
