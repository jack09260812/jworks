package com.github.utils.clone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by jinwei.li on 2017/8/28 0028.
 */
public class CloneUtil {

    private static Logger logger = LoggerFactory.getLogger(CloneUtil.class);

    /**
     * 浅拷贝
     *
     * @param t 需实现{@link Cloneable}接口
     * @return 浅拷贝对象
     */
    public static <T extends Cloneable> T simpleClone(T t) {
        return (T) t.clone();
    }

    /**
     * 深拷贝
     *
     * @param t 需实现{@link Serializable}接口
     * @return
     */
    //TODO 优化速度
    public static <T extends Serializable> T clone(T t) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(t);
        oos.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        T clone = null;
        try {
            clone = (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            logger.error("拷贝异常，源类不存在！", e);
        }
        ois.close();
        return clone;
    }
}
