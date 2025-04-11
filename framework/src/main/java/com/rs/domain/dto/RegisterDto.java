package com.rs.domain.dto;

import lombok.Data;

/**
 * 用户注册数据传输对象
 */
@Data
public class RegisterDto {
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
} 