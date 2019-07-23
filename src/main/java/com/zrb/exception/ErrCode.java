package com.zrb.exception;

/**
 * Author:  Hardy
 * Date:    2018/7/18 20:05
 * Description: 业务异常码
 **/
public enum ErrCode {

    // 40xxx
    NOT_FOUND_TRANS(40001, "找不到资金"),

    // 非业务异常
    BAD_PARAM(4000, "参数错误"),
    NOT_FOUND(4004, "找不到"),
    DUPLICATE(4022, "重复提交"),
    CALL_ERROR(8000, "调用失败"),
    SYS_ERROR(9999, "系统错误");

    private int code;
    private String message;

    ErrCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
