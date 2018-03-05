package com.github.utils.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by jinwei.li on 2017/5/8 0008.
 */
public class CollectionsUtil {

    /**
     * 判断集合为空
     * @param collection
     * @return
     */
    public boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 数组是否为空
     *
     * @param <T> 数组元素类型
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty(final T... array) {
        return array == null || array.length == 0;
    }

    /**
     * 获取集合第一个值
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> T getFirstVal(Collection<T> collection) {
        if (collection == null || collection.isEmpty())
            return null;
        else {
            if (collection instanceof List)
                return ((List<T>) collection).get(0);
            else {
                Iterator<T> iterator = collection.iterator();
                return iterator.next();
            }
        }
    }
}
