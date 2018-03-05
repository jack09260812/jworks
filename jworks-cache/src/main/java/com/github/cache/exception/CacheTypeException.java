package com.github.cache.exception;

/**
 * Created by jinwei on 17-8-25.
 */
public class CacheTypeException extends IllegalArgumentException {


    /**
     * 缓存类型异常，key或value非法
     *
     * @param message
     */
    public CacheTypeException(String message) {
        super(message);
    }

    /**
     * 缓存类型异常，key或value非法
     *
     * @param message
     * @param cause
     */
    public CacheTypeException(String message, Throwable cause) {
        super(message, cause);
    }

}
