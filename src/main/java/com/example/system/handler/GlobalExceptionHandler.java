package com.example.system.handler;

import com.example.system.exception.UnAuthorizedException;
import com.example.system.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResult<String> mostException(Exception e) {
        e.printStackTrace();
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    @ResponseBody
    public CommonResult<String> unAuthorizedException(UnAuthorizedException e) {
        e.printStackTrace();
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResult<String> handleException(IllegalArgumentException e) {
        e.printStackTrace();
        return CommonResult.failed(e.getMessage());
    }

}
