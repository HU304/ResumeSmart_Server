package com.rs.controller;

import com.rs.response.Result;
import com.rs.service.ResumeRecordsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 简历控制器
 */
@RestController
@RequestMapping("/resume")
@Slf4j
public class ResumeController {

    @Resource
    private ResumeRecordsService recordsService; //简历记录处理


    /**
     * 上传简历文件 解析
     * @param files
     * @return
     */
    @PostMapping("/upload")
    public Result parseResume(@RequestParam("file") MultipartFile[] files) {
        return recordsService.parseResumeFiles(files);
    }


    /**
     * 获取简历列表
     * @return
     */
    @GetMapping("list")
    public Result list() {
        return Result.success(recordsService.list());
    }

}