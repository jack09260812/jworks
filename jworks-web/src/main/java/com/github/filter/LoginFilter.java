package com.github.filter;

import com.github.security.model.AuthUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by jinwei.li on 2017/8/23 0023.
 */
public class LoginFilter extends CustomizeOncePerRequestFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AuthUser user = (AuthUser) session.getAttribute("user");
        if(user == null){
        }
    }
}
