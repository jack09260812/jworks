package com.github.rpc.utils;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jinwei.li on 2016/12/31.
 */
public class PropertiesHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(PropertiesHelper.class);
    private static Properties p;

    static {
        InputStream is = PropertiesHelper.class.getResourceAsStream("/config.properties");
        p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("配置文件加载失败", e);
        }
    }

    /**
     * 判断是否包含属性
     *
     * @param key
     * @return
     */
    public static boolean containKey(String key) {
        Preconditions.checkNotNull(key);
        return p.containsKey(key);
    }

    /**
     * 返回配置信息
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        if (containKey(key))
            return p.getProperty(key);
        else
            return null;
    }

    public static int getIntValue(String key) {
        String value = getValue(key);
        if (value == null)
            return -1;
        return Integer
                .parseInt(value);
    }

    public static double getDoubleValue(String key) {
        String value = getValue(key);
        Preconditions.checkNotNull(value);
        return Double
                .parseDouble(value);
    }

    public static boolean getBooleanValue(String key) {
        String value = getValue(key);
        Preconditions.checkNotNull(value);
        return Boolean
                .parseBoolean(value);
    }
}
