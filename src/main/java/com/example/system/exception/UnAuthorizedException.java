package com.example.system.exception;


/**
 * 未认证异常类
 */
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String message) {
        super(message);
    }
}
