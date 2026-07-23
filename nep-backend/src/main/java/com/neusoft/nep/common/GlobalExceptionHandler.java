package com.neusoft.nep.common;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理，避免堆栈直接暴露给前端
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public R handleBusinessException(BusinessException e) {
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public R handleRuntimeException(RuntimeException e) {
        return R.error(e.getMessage() != null ? e.getMessage() : "运行时异常");
    }

    @ExceptionHandler(BindException.class)
    public R handleBindException(BindException e) {
        FieldError fieldError = e.getFieldError();
        String msg = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        return R.error(msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        return R.error(msg);
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        return R.error("系统异常，请稍后重试");
    }
}
