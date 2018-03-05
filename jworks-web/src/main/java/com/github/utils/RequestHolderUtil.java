package com.github.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jinwei.li on 2017/6/8 0008.
 * current thread -> request -> session -> info
 */
public class RequestHolderUtil {

    private static Logger logger = LoggerFactory.getLogger(RequestHolderUtil.class);

    /**
     * key:thread name
     * value:request
     */
    private static Map<String, HttpServletRequest> requests = new ConcurrentHashMap<>();

    /**
     * 根据key获取session结果信息
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getInfo(String key) {
        Thread t = Thread.currentThread();
        String currentThreadName = t.getName();
        HttpServletRequest request = requests.get(currentThreadName);
        if (request == null) {
            logger.error("{}线程获取request失败", currentThreadName);
            return null;
        }
        HttpSession session = request.getSession();
        return (T) session.getAttribute(key);
    }


    /**
     * 设置request
     *
     * @param request
     */
    public static void setRequest(HttpServletRequest request) {
        Thread t = Thread.currentThread();
        String currentThreadName = t.getName();
        setRequest(currentThreadName, request);
    }

    /**
     * 设置request
     *
     * @param request
     */
    public static void setRequest(String currentThreadName, HttpServletRequest request) {
        requests.put(currentThreadName, request);
    }

    /**
     * 清除request
     */
    public static void clearRequest() {
        Thread t = Thread.currentThread();
        String currentThreadName = t.getName();
        clearRequest(currentThreadName);
    }

    /**
     * 清除request
     */
    public static void clearRequest(String currentThreadName) {
        requests.remove(currentThreadName);
    }
}
