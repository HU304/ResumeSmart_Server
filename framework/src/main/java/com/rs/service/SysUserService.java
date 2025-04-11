package com.rs.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rs.domain.dto.RegisterDto;
import com.rs.domain.entity.SysUser;
import com.rs.response.Result;

/**
 * 用户表(SysUser)表服务接口
 *
 * @author makejava
 * @since 2025-03-17 13:16:26
 */
public interface SysUserService extends IService<SysUser> {

    Result userInfo();
    
    /**
     * 用户注册
     *
     * @param registerDto 注册信息
     * @return 注册结果
     */
    Result register(RegisterDto registerDto);
}

