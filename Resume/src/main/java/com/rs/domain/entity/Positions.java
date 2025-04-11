package com.rs.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * 岗位表, 存储公司内所有岗位的基本信息(Positions)表实体类
 *
 * @author makejava
 * @since 2025-04-11 22:28:50
 */
@TableName("positions")
@Data
public class Positions {
    //岗位ID, 唯一标识每个岗位
    @TableId
    private Integer positionId;
    //岗位名称
    private String positionName;
    //岗位描述, 详细介绍岗位职责等
    private String positionDescription;
    //创建时间, 记录岗位信息的创建时间
    private Date createTime;
    //更新时间, 每次更新时自动更新
    private Date updatedTime;
    //岗位状态, 表示该岗位是否启用，默认1 启用
    private Integer status;

}

