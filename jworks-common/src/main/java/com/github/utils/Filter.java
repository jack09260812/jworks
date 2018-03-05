package com.github.utils;

/**
 * 过滤器接口
 *
 */
public interface Filter<T> {
	boolean accept(T t);
}