package com.rs.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rs.domain.entity.ResumeRecords;
import com.rs.response.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * 简历记录表，存储简历上传、处理和状态信息(ResumeRecords)表服务接口
 *
 * @author makejava
 * @since 2025-04-11 16:05:08
 */
public interface ResumeRecordsService extends IService<ResumeRecords> {

    Result parseResumeFiles(MultipartFile[] files);
}

