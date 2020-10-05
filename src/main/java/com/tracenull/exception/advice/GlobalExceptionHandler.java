package com.tracenull.exception.advice;

import com.tracenull.eu.CodeEnum;
import com.tracenull.exception.EmptyResutlException;
import com.tracenull.exception.RequestParamException;
import com.tracenull.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 查询结果为空时处理
     *
     * @param e
     * @return
     */

    @ExceptionHandler(EmptyResutlException.class)
    public R emptyResultExceptionHandler(EmptyResutlException e) {
        LOGGER.error("查询结果为空：{}", e.getMessage());
        R result = new R();
        result.fillCode(e.getCode(), e.getMessage());
        return result;
    }

    /**
     * 请求参数错误时处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RequestParamException.class)
    public R requestParamExceptionHandler(RequestParamException e) {
        LOGGER.error("请求参数不合法：{}", e.getMessage());
        R result = new R();
        result.fillCode(e.getCode(), e.getMessage());
        return result;
    }

    /**
     * 处理其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R exceptionHandler(Exception e) {
        LOGGER.error("未知异常：{}", e.getMessage());
        R result = new R();
        result.fillCode(CodeEnum.ERROR);
        return result;
    }
}
