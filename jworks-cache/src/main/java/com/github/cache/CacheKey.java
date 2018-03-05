package com.github.cache;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by jinwei on 17-8-25.
 */
@Data
public class CacheKey implements Serializable {
    private static final long serialVersionUID = 140217028003600192L;
    private String region;
    private String key;

    public CacheKey(String region, String key) {
        this.region = region;
        this.key = key;
    }

    /**
     * 生成cache key
     * @param region
     * @param key
     * @return
     */
    public static CacheKey gen(String region, String key) {
        return new CacheKey(region, key);
    }
}
