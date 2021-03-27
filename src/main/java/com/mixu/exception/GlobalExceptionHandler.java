package com.mixu.exception;

//全局异常处理器，处理整个web controller的异常

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //处理数学异常和空指针异常
    @ExceptionHandler(value = {ArithmeticException.class,NullPointerException.class})
    public String handleException01(Exception e){
        log.error("异常是：{}",e);
        //视图地址
        return "login";
    }
}
