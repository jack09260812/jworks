package com.github.interceptor;

import com.github.utils.RequestHolderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author jinwei.li
 * @Date 2017/6/8 0008
 * @Description
 */
public class RequestHolderInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String threadName = Thread.currentThread().getName();
        RequestHolderUtil.setRequest(threadName, request);
        logger.debug("{}注入request信息", threadName);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String threadName = Thread.currentThread().getName();
        RequestHolderUtil.clearRequest(threadName);
        logger.debug("{}清除request信息", threadName);
        super.afterCompletion(request, response, handler, ex);
    }
}
