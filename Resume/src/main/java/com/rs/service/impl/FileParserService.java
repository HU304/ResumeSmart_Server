package com.rs.service.impl;

import com.rs.exception.FileParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
public class FileParserService {

    /**
     * 解析PDF文件内容
     */
    public String parsePdf(InputStream inputStream) throws FileParseException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            if (document.isEncrypted()) {
                throw new FileParseException("加密的PDF文件不支持解析");
            }
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            log.error("PDF解析失败", e);
            throw new FileParseException("PDF解析失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("未知错误", e);
            throw new FileParseException("PDF解析失败: " + e.getMessage());
        }
    }

    /**
     * 解析Word文件内容（支持docx和doc）
     */
    public String parseWord(InputStream inputStream) throws FileParseException {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            StringBuilder text = new StringBuilder();
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                text.append(paragraph.getText()).append("\n");
            }
            return text.toString();
        } catch (IOException e) {
            log.error("Word解析失败", e);
            throw new FileParseException("Word解析失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("未知错误", e);
            throw new FileParseException("Word解析失败: " + e.getMessage());
        }
    }

    /**
     * 通用文件解析入口
     * @param filePath 文件路径
     * @param originalFilename 文件名
     * @return
     * @throws FileParseException
     */
    public String parseFile(Path filePath, String originalFilename) throws FileParseException {
        try (InputStream is = Files.newInputStream(filePath)) {
            String filename = originalFilename.toLowerCase();

            if (filename.endsWith(".pdf")) {
                return parsePdf(is);
            } else if (filename.endsWith(".docx") || filename.endsWith(".doc")) {
                return parseWord(is);
            }
            throw new FileParseException("不支持的文件类型: " + originalFilename);
        } catch (IOException e) {
            throw new FileParseException("文件读取失败: " + e.getMessage());
        }
    }
}