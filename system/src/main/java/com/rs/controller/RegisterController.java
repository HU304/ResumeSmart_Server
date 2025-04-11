package com.rs.controller;

import com.rs.domain.dto.RegisterDto;
import com.rs.response.Result;
import com.rs.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户注册控制器
 */
@RestController
public class RegisterController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 用户注册
     *
     * @param registerDto 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDto registerDto) {
        return sysUserService.register(registerDto);
    }
} 