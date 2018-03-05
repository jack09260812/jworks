package com.github.utils.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;

/**
 * Created by jinwei.li on 2017/1/1.
 */
public class BeanUtil {

    /**
     * 判断是否实现了父类或接口
     * @param parent
     * @param child
     * @return
     */
    public static boolean isExtendFrom(Class parent,Class child){
        Preconditions.checkNotNull(parent);
        return parent.isAssignableFrom(child);
    }

}
