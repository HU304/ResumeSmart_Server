package com.rs.handler.security;


import com.rs.response.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    DataIntegrityViolationException e;

    @ExceptionHandler
    public Result handlerException(Exception e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    @ExceptionHandler
    public Result handlerException(AccessDeniedException e) {
        e.printStackTrace();
        return Result.error("权限不足");
    }

    @ExceptionHandler
    public Result handlerSQLException(DataAccessException e) {
        e.printStackTrace();
        return Result.error("数据库操作失败");
    }
}
