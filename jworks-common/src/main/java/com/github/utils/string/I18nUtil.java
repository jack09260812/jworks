package com.github.utils.string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 请求响应信息
 */
public class I18nUtil {

    private static Logger logger = LoggerFactory.getLogger(I18nUtil.class);
    private static Locale locale;
    private static ResourceBundle rb;

    static {
        // 获得缺省的系统区域
        locale = Locale.getDefault();
        // 获得资源文件
        rb = ResourceBundle.getBundle("response", locale);
    }

    /**
     * 获取配置信息
     *
     * @param code
     * @return
     */
    public static String getMessage(String code) {
        if (StringUtil.isNull(code)) {
            logger.error("code不能为空");
            return null;
        }
        // 获得相应的key值
        String str = rb.getString(code);
        return str;
    }

    /**
     * 获取结果信息
     *
     * @param code
     * @param objs
     */
    public static String getMessage(String code, Object... objs) {
        if (StringUtil.isNull(code)) {
            logger.error("code不能为空");
            return null;
        }
        // 获得相应的key值
        String template = rb.getString(code);
        // 格式化参数
        Object[] params = objs;

        // 格式化参数,返回格式后的字符串
        String result = MessageFormat.format(template, params);

        return result;
    }
}