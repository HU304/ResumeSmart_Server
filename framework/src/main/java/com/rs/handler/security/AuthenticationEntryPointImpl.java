package com.rs.handler.security;


import com.alibaba.fastjson.JSON;
import com.rs.constants.HttpStatus;
import com.rs.response.Result;
import com.rs.uitils.ResponseUtils;
import com.rs.uitils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证失败处理类
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse
            response, AuthenticationException authException)
             {
        authException.printStackTrace();

        Result result = null;
        if (authException instanceof BadCredentialsException) {
            result =
                    Result.error(HttpStatus.FORBIDDEN, "认证或授权失败");
        } else if (authException instanceof InsufficientAuthenticationException) {
            result = Result.error(HttpStatus.UNAUTHORIZED, "请先登录再进行操作");
        } else {
            result =
                    Result.error(HttpStatus.FORBIDDEN,
                            "认证或授权失败");
        }
//响应给前端
                 String json = JSON.toJSONString(result);
                 ResponseUtils.write(response, json);
    }
}