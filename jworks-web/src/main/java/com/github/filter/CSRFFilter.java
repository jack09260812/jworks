package com.github.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jinwei.li on 2017/8/23 0023.
 * refer check
 * token 验证header中的token信息（登录时返回token）
 */
public class CSRFFilter extends CustomizeOncePerRequestFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
