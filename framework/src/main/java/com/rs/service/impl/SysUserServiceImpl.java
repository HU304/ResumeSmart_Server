package com.rs.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.domain.dto.RegisterDto;
import com.rs.mapper.SysUserMapper;
import com.rs.domain.entity.LoginUser;
import com.rs.domain.entity.SysUser;
import com.rs.domain.vo.UserInfoVo;
import com.rs.response.Result;
import com.rs.service.SysUserService;
import com.rs.uitils.BeanCopyUtils;
import com.rs.uitils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户表(SysUser)表服务实现类
 *
 * @author makejava
 * @since 2025-03-17 13:16:26
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public Result userInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getSysUser(), UserInfoVo.class);
        return Result.success(userInfoVo);
    }
    
    @Override
    @Transactional
    public Result register(RegisterDto registerDto) {
        // 判断用户名是否已存在
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, registerDto.getUsername());
        SysUser existUser = getOne(queryWrapper);
        if (existUser != null) {
            return Result.error("用户名已存在");
        }
        
        // 判断邮箱是否已被使用
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getEmail, registerDto.getEmail());
        existUser = getOne(queryWrapper);
        if (existUser != null) {
            return Result.error("邮箱已被使用");
        }
        
        // 创建用户对象
        SysUser user = new SysUser();
        user.setUsername(registerDto.getUsername());
        // 加密密码
        user.setPassword(SecurityUtils.encryptPassword(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        
        // 保存用户
        save(user);
        
        return Result.success("注册成功");
    }
}

