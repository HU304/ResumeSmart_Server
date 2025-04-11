package com.rs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.domain.entity.Positions;
import com.rs.mapper.PositionsMapper;
import com.rs.service.PositionsService;
import org.springframework.stereotype.Service;

/**
 * 岗位表, 存储公司内所有岗位的基本信息(Positions)表服务实现类
 *
 * @author makejava
 * @since 2025-04-11 22:28:51
 */
@Service
public class PositionsServiceImpl extends ServiceImpl<PositionsMapper, Positions> implements PositionsService {

}

