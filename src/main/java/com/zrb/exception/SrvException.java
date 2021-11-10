package com.zrb.exception;

import com.riverstar.tool.StringTool;

/**
 * Author:  Hardy
 * Date:    2018/7/18 20:05
 * Description: 业务异常
 **/
public class SrvException extends RuntimeException {

    private ErrCode errCode;

    public SrvException(ErrCode errCode) {
        super(errCode.message());
        this.errCode = errCode;
    }

    public SrvException(ErrCode errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public SrvException(ErrCode errCode, String format, Object... args) {
        super(StringTool.formatStr(format, args));
        this.errCode = errCode;
    }

    public int getCode() {
        return errCode.code();
    }
}
