package com.dsltyyz.bundle.common.token.exception;

/**
 * Description:
 * 缺少token异常
 *
 * @author: dsltyyz
 * @date: 2020/10/14
 */
public class RequireTokenException extends RuntimeException{

    public RequireTokenException(){
        super();
    }

    public RequireTokenException(String msg){
        super(msg);
    }
}
