package com.github.filter;

import com.github.utils.string.StringUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jinwei.li on 2017/8/23 0023.
 * 自定义filter扩展类，增加白名单功能
 */
public abstract class CustomizeOncePerRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String whiteList = getFilterConfig().getInitParameter("whiteList");
        if (!StringUtil.isNull(whiteList)) {
            String[] list = whiteList.split(",");
            String uri = request.getServletPath();
            for (String str : list) {
                if (str.equals(uri)) {
                    //白名单不进行过滤
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }
        doFilter(request, response, filterChain);
    }

    protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;
}
