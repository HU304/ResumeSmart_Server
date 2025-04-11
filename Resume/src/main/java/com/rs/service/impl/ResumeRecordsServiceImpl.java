package com.rs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.config.MQConfig;
import com.rs.enums.ProcessStatus;
import com.rs.mapper.ResumeRecordsMapper;
import com.rs.domain.entity.ResumeRecords;
import com.rs.response.Result;
import com.rs.service.FileStorageService;
import com.rs.service.ResumeRecordsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service("resumeRecordsService")
@Slf4j
public class ResumeRecordsServiceImpl extends ServiceImpl<ResumeRecordsMapper, ResumeRecords> implements ResumeRecordsService {

    @Resource
    private ResumeRecordsMapper resumeRecordsMapper;
    @Resource
    private FileStorageService storageService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public Result parseResumeFiles(MultipartFile[] files) {
        List<String> errorMessages = new ArrayList<>();
        int successCount = 0;

        for (MultipartFile file : files) {
            String resumeId = UUID.randomUUID().toString();
            ResumeRecords record = new ResumeRecords();
            record.setResumeId(resumeId);
            record.setStatus(ProcessStatus.PENDING);
            record.setOriginalFilename(file.getOriginalFilename());
            //获取文件后缀
            String suffix = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            // 设置文件类型
            record.setFileType(suffix);
            record.setCreateTime(LocalDateTime.now());

            // 先插入记录，此时filePath为空
            resumeRecordsMapper.insert(record);

            try {
                String filePath = storageService.storeFile(file, resumeId);
                record.setFilePath(filePath);
                resumeRecordsMapper.updateById(record);

                // 发送MQ消息
                rabbitTemplate.convertAndSend(
                        MQConfig.RESUME_EXCHANGE,
                        MQConfig.ROUTING_KEY,
                        resumeId
                );
                successCount++;
            } catch (Exception e) {
                log.error("简历文件处理失败: {}", file.getOriginalFilename(), e);
                record.setStatus(ProcessStatus.FAILED);
                record.setErrorMessage("简历文件处理失败: " + e.getMessage());
                resumeRecordsMapper.updateById(record);
                errorMessages.add(file.getOriginalFilename() + ": " + e.getMessage());
            }
        }

        if (!errorMessages.isEmpty()) {
            String message = String.format("成功处理%d个文件，失败%d个。失败原因简历详情",
                    successCount, errorMessages.size(), String.join("; ", errorMessages));
            return Result.error(message);
        } else {
            return Result.success("简历上传成功");
        }
    }
}