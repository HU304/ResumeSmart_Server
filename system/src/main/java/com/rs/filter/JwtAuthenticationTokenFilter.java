package com.rs.filter;


import com.alibaba.fastjson.JSON;
import com.rs.constants.CacheConstants;
import com.rs.constants.HttpStatus;
import com.rs.domain.entity.LoginUser;
import com.rs.response.Result;
import com.rs.uitils.JwtUtil;
import com.rs.uitils.RedisCache;
import com.rs.uitils.WebUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * 前置过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException,
            IOException {
//获取请求头中的token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
//说明该接口不需要登录 直接放行
            filterChain.doFilter(request, response);
            return;
        }
//解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
//token超时 token非法
//响应告诉前端需要重新登录
            Result result = Result.error(HttpStatus.UNAUTHORIZED,"token失效，请重新登录");
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId = claims.getSubject();
//从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject(CacheConstants.BLOG_LOGIN_USER_KEY + userId);
//如果获取不到
        if (Objects.isNull(loginUser)) {
//说明登录过期 提示重新登录
            Result result = Result.error(HttpStatus.FORBIDDEN,"授权过期");
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
//存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}