package com.rs.exception;

/**
 * 文件解析异常
 */
public class FileParseException extends Exception {
    public FileParseException(String message) {
        super(message);
    }
    
    public FileParseException(String message, Throwable cause) {
        super(message, cause);
    }
}