package com.rs.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rs.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(SysUser)表数据库访问层
 *
 * @author makejava
 * @since 2025-03-17 13:16:26
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}

