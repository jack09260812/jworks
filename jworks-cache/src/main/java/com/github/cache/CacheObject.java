package com.github.cache;

import lombok.Data;

import java.io.Serializable;

/**
 * 缓存对象，保存缓存值，过期时间等
 * Created by jinwei on 17-8-25.
 */
@Data
public class CacheObject implements Serializable {
    private static final long serialVersionUID = -5659386540257412806L;
    private String value;
}
