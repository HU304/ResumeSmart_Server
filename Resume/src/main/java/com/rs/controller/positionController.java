package com.rs.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rs.domain.entity.Positions;
import com.rs.response.Result;
import com.rs.service.PositionsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/position")
public class positionController {

    @Resource
    private PositionsService positionsService;

    /**
     * 获取岗位列表
     */
    @GetMapping("/list")
    public Result list(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);


        List<Positions> list = positionsService.list();
        PageInfo<Positions> info = new PageInfo<>(list);

        return Result.success(info);
    }
}
