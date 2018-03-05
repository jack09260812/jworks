package com.github.utils;

import com.github.utils.string.StringUtil;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 通过-DconfigPath=***来指定配置文件位置
 * Created by jinwei.li on 2016/12/31.
 */
public class PropertiesHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(PropertiesHelper.class);
    private static Properties p;

    static {
        InputStream is = null;
        String configPath = System.getProperty("configPath");
        if (StringUtil.isNull(configPath)) {
            is = PropertiesHelper.class.getResourceAsStream("/config.properties");
        } else {
            try {
                File file = new File(configPath);
                //当为路径时，查找文件夹下配置文件
                if (file.isDirectory())
                    file = new File(configPath + "/config.properties");
                is = new FileInputStream(file);
                LOGGER.info("加载配置文件：{}", configPath);
            } catch (FileNotFoundException e) {
                LOGGER.error("配置文件不存在", e);
            }
        }
        p = new Properties();
        try {
            p.load(is);
        } catch (Exception e) {
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
        Preconditions.checkNotNull(value);
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
