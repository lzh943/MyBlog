package com.hello.handler.exception;

import com.hello.domain.ResponseResult;
import com.hello.enums.AppHttpCodeEnum;
import com.hello.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseResult badCredentialsException(BadCredentialsException e){
        log.error("出现了异常！ {}",e);
        return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),AppHttpCodeEnum.NEED_LOGIN.getMsg());
    }
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}