package com.rs.config;

import com.rs.domain.entity.ResumeRecords;
import com.rs.enums.ProcessStatus;
import com.rs.exception.FileParseException;
import com.rs.exception.UnsupportedFileTypeException;
import com.rs.service.ResumeRecordsService;
import com.rs.service.impl.FileParserService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * 简历处理消息消费者
 */
@Component
public class ResumeProcessor {

    @Resource
    private ResumeRecordsService resumeRecordsService;
    @Resource
    private FileParserService fileParserService;

    /**
     * 处理简历解析消息 消费
     *
     * @param resumeId
     */
    @RabbitListener(queues = MQConfig.RESUME_QUEUE)
    public void processResume(String resumeId) {
        System.out.println("==========收到简历解析消息=============：" + resumeId);
        ResumeRecords record = resumeRecordsService.getById(resumeId);
        if (record == null) {
            throw new RuntimeException("Resume not found"); // 简历不存在
        }

        try {
            record.setStatus(ProcessStatus.PROCESSING); //状态改为处理中
            resumeRecordsService.updateById(record); // 更新状态

            // 解析文件
            Path filePath = Paths.get(record.getFilePath());
            String content = parseFile(filePath, record.getOriginalFilename());

            //判断是否解析后的文本
            if (content.trim().length() < 200) {
                record.setErrorMessage("简历内容太短，可能为无效简历");
            }

            record.setContent(content);
            record.setStatus(ProcessStatus.SUCCESS);
            record.setProcessTime(LocalDateTime.now());
            resumeRecordsService.updateById(record); //解析完成后保存文件

        } catch (Exception e) {
            //解析出现异常 变更简历状态为处理失败
            record.setStatus(ProcessStatus.FAILED);
            record.setErrorMessage("简历解析失败:" + e.getMessage());
            resumeRecordsService.updateById(record);
        }
    }


    /**
     * 解析文件
     *
     * @param filePath
     * @param filename
     * @return
     * @throws IOException
     */
    private String parseFile(Path filePath, String filename) {

        try {
            return fileParserService.parseFile(filePath, filename);
        } catch (FileParseException e) {
            throw new UnsupportedFileTypeException("不支持的文件类型");
        }
    }
}