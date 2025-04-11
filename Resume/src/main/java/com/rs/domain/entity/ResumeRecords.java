package com.rs.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rs.enums.ProcessStatus;
import lombok.Data;

import java.time.LocalDateTime;

@TableName(value = "resume_records")
@Data
public class ResumeRecords {
    @TableId
    private String resumeId; // UUID 简历id
    private ProcessStatus status; // 状态
    private String originalFilename; // 原始文件名
    private String filePath; // 存储路径
    private String fileType; // 文件类型
    private String content; // 文件内容
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime processTime; // 处理时间
    private String errorMessage; // 错误信息
}

