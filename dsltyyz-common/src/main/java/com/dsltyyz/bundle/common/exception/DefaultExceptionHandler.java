package com.dsltyyz.bundle.common.exception;

import com.dsltyyz.bundle.common.response.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
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
        return new CommonResponse(600L, e.getMessage());
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public CommonResponse handlerUndeclaredThrowableException(UndeclaredThrowableException e) {
        log.info(e.getMessage());
        return new CommonResponse(700L, e.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public CommonResponse handlerMalformedJwtException(MalformedJwtException e) {
        log.error(e.getMessage());
        return new CommonResponse(800L, "token非法");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public CommonResponse handlerExpiredJwtException(ExpiredJwtException e) {
        log.error(e.getMessage());
        return new CommonResponse(801L, "token过期");
    }

}
