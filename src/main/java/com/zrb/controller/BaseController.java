package com.zrb.controller;


import com.riverstar.tool.DebugTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.zrb.exception.ErrCode;
import com.zrb.exception.SrvException;
import com.zrb.model.base.RestResponse;


/**
 * Author:  Hardy
 * Date:    2018/7/18 20:05
 * Description: RestController基类 异常拦截
 **/
public abstract class BaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(BindException.class)
    public RestResponse validExcept(BindException e) {
        return new RestResponse(ErrCode.BAD_PARAM.code(), DebugTool.getMessage(e));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public RestResponse validExcept(MethodArgumentTypeMismatchException e) {
        return new RestResponse(ErrCode.BAD_PARAM.code(), DebugTool.getMessage(e));
    }

    @ExceptionHandler(SrvException.class)
    protected RestResponse except(SrvException e) {
        return new RestResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected RestResponse except(Exception e) {
        log.error("[SYS_ERROR] error: ", e);
        return new RestResponse(ErrCode.SYS_ERROR.code(), DebugTool.getMessage(e));
    }
}
