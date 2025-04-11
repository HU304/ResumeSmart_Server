package com.rs.controller;

import com.rs.constants.CacheConstants;
import com.rs.domain.entity.LoginUser;
import com.rs.service.LoginService;
import com.rs.uitils.RedisCache;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rs.domain.dto.LoginDto;
import com.rs.response.Result;

@RestController
@RequestMapping()
public class LoginController {


    @Resource
    private LoginService loginService;

    @Resource
    private RedisCache redisCache;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto) {
        return loginService.login(loginDto);
    }

    @PostMapping("/logout")
    public Result logout() {
        //获取token 解析获取userid
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userid
        Long userId = loginUser.getSysUser().getUserId();
        //删除redis中的用户信息
        redisCache.deleteObject(CacheConstants.BLOG_LOGIN_USER_KEY + userId);
        return Result.success();
    }


}
