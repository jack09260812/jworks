package com.github.utils.clone;

/**
 * @Author jinwei.li
 * @Date 2017/8/28 0028
 * @Description clone
 */
public interface Cloneable<T> extends java.lang.Cloneable {

    /**
     * 克隆当前对象，浅复制
     *
     * @return
     */
    T clone();
}
