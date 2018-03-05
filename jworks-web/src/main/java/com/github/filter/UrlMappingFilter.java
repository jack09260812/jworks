package com.github.filter;

import com.github.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by jinwei.li on 2017/5/18 0018.
 */
public class UrlMappingFilter extends OncePerRequestFilter {

    private Set<PatternsRequestCondition> requestConditions;
    //拦截是否生效
    private boolean invalid;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public UrlMappingFilter(boolean invalid, Set<PatternsRequestCondition> requestConditions) {
        this.invalid = invalid;
        this.requestConditions = requestConditions;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (invalid) {
            logger.debug("不进行拦截");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String uri = httpServletRequest.getServletPath();
        String contextPath = httpServletRequest.getContextPath();
        //静态资源直接跳过拦截
        if (uri.matches("^/static/.*") || uri.matches(".*.json$") || uri.matches(".*.ico$") || uri.matches(".*.jsp$") || uri.matches(".*.html$") || uri.matches(".*.ftl$") || uri.matches(".*.js$") || uri.matches(".*.css$")) {
            logger.debug("{}为静态资源，不进行拦截", uri);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if (uri.matches("^/druid")) {
            logger.debug("{}匹配成功", uri);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String excludePaths = getFilterConfig().getInitParameter("excludePaths");
        if (!StringUtil.isNull(excludePaths)) {
            for (String excludePath : excludePaths.split(",")) {
                if (uri.matches(excludePath)) {
                    logger.debug("{}匹配成功", uri);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
            }
        }
        for (PatternsRequestCondition requestCondition : requestConditions) {
            if (requestCondition.getMatchingCondition(httpServletRequest) != null) {
                logger.debug("{}匹配成功", uri);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        }
        logger.debug("{}拦截成功，跳转至首页", uri);
        httpServletRequest.getRequestDispatcher( "/").forward(httpServletRequest, httpServletResponse);
    }
}
