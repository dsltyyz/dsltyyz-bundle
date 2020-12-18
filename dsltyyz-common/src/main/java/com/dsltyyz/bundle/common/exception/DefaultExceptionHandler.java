package com.dsltyyz.bundle.common.exception;

import com.dsltyyz.bundle.common.response.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Description:
 * 默认异常处理
 *
 * @author: dsltyyz
 * @date: 2020-8-28
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    /**************自定义异常 1+*****************/

    /**
     * JWT token非法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MalformedJwtException.class)
    public CommonResponse handlerMalformedJwtException(MalformedJwtException e) {
        log.error(e.getMessage());
        return new CommonResponse(1L, "token非法");
    }

    /**
     * JWT token过期
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public CommonResponse handlerExpiredJwtException(ExpiredJwtException e) {
        log.error(e.getMessage());
        return new CommonResponse(2L, "token过期");
    }

    @ExceptionHandler(JwtException.class)
    public CommonResponse handlerIllegalAccessException(JwtException e) {
        log.info(e.getMessage());
        return new CommonResponse(3L, e.getMessage());
    }

    /**************常见系统异常 500+*****************/

    @ExceptionHandler(Exception.class)
    public CommonResponse handlerException(Exception e) {
        log.error(e.getMessage());
        return new CommonResponse(500L, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public CommonResponse handlerRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return new CommonResponse(501L, "运行时异常");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResponse handlerIllegalArgumentException(IllegalArgumentException e) {
        log.info(e.getMessage());
        return new CommonResponse(502L, e.getMessage());
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public CommonResponse handlerUndeclaredThrowableException(UndeclaredThrowableException e) {
        log.info(e.getMessage());
        return new CommonResponse(503L, e.getMessage());
    }

}
