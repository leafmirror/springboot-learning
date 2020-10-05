package com.tracenull.eu;


/**
 * https://mp.weixin.qq.com/s/uyNerDDvYafHXh_OImbOww
 */
public enum CodeEnum {
    SUCCESS(0, "请求成功"),
    ERROR(500, "未知异常"),
    ERROR_EMPTY_RESULT(1001, "查询结果为空"),
    ERROR_INCOMPLETE_RESULT(1002, "请求参数不全");

    private int code;
    private String message;

    CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
