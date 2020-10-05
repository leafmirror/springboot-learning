package com.tracenull.exception;

import com.tracenull.eu.CodeEnum;
import lombok.Data;

@Data
public class RequestParamException extends RuntimeException {
    private static final long serialVersionUID = 4748844811214637041L;
    private int code;
    private String message;

    public RequestParamException(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }
    public RequestParamException(int code,String message){
        this.code = code;
        this.message = message;
    }
}
