package com.tracenull.exception;

import com.tracenull.eu.CodeEnum;
import lombok.Data;

@Data
public class EmptyResutlException extends RuntimeException {
    private static final long serialVersionUID = -8839210969758687047L;
    private int code;
    private String message;

    public EmptyResutlException(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }

    public EmptyResutlException(int code,String message){
        this.code = code;
        this.message = message;
    }

}
