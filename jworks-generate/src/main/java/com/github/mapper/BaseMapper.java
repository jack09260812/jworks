package com.github.mapper;

import java.util.List;

/**
 * Created by jinwei.li on 2017/5/3 0003.
 */
public interface BaseMapper<T, K> {

    //插入
    int insert(T entity);

    //根据主键更新
    int update(T entity);

    int updateByIds(T entity);

    //根据主键删除
    int deleteByPrimaryKey(K id);

    int deleteByIds(T entity);

    int delete(T entity);

    //根据主键查询
    T selectByPrimaryKey(K id);

    List<T> select(T entity);
}
