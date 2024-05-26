package com.example.system.filter;

import com.example.system.exception.UnAuthorizedException;
import com.example.system.util.JwtTokenUtil;
import com.example.system.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class AuthenticationFilter implements Filter {

    private String[] excludedUris;

    @Autowired
    public JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void init(FilterConfig filterConfig) {
        String excludedUrisParam = filterConfig.getInitParameter("excludedUris");
        if (excludedUrisParam != null && !excludedUrisParam.isEmpty()) {
            excludedUris = excludedUrisParam.split(",");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        try {
            authorize(httpRequest);
            chain.doFilter(request, response);
        } catch (Exception e) {
            // 传递异常信息
            request.setAttribute("exception", e);
            // 指定处理该请求的处理器
            request.getRequestDispatcher("/exception").forward(request, response);
        }
    }

    private boolean isExcluded(HttpServletRequest httpRequest) {
        boolean isExcluded = false;
        String requestURI = httpRequest.getRequestURI();
        for (String uri : excludedUris) {
            if (requestURI.endsWith(uri)) {
                isExcluded = true;
                break;
            }
        }
        return isExcluded;
    }

    private void authorize(HttpServletRequest httpRequest) {
        boolean isExcludedUri = isExcluded(httpRequest);
        if (!isExcludedUri) {
            String token = httpRequest.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                throw new UnAuthorizedException("用户未登录");
            } else {
                if (jwtTokenUtil.isTokenExpired(token)) {
                    throw new UnAuthorizedException("用户登录已过期，请重新登录");
                }
                String jobNumber = jwtTokenUtil.getJobNumberFromToken(token);
                String key = "token:" + jobNumber;
                String redisToken = (String) redisUtil.get(key);
                if (redisToken == null) {
                    throw new UnAuthorizedException("请登录后再访问该资源");
                } else {
                    String newToken = jwtTokenUtil.refreshHeadToken(token);
                    redisUtil.setWithExpire(key, newToken, 7, TimeUnit.DAYS);
                }
            }
        }
    }
}
