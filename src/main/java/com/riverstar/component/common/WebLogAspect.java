package com.riverstar.component.common;

import com.riverstar.exception.ErrCode;
import com.riverstar.exception.SrvException;
import com.riverstar.model.base.RestResponse;
import com.riverstar.tool.DebugTool;
import com.riverstar.tool.HttpTool;
import com.riverstar.tool.JsonTool;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * Author: Hardy
 * Date:   2018/7/27 10:31
 * Description:
 **/
@Aspect
@Component
public class WebLogAspect {

    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Autowired
    private HttpServletRequest request;

    @Pointcut("execution(public * com.zrb.controller.*.*(..)) && !execution(public * com.riverstar.controller.AliveController.*(..)) ")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length != 0) {
            if (joinPoint.getArgs()[0] instanceof BindException) {
                BindException err = (BindException) joinPoint.getArgs()[0];
                log.info(HttpTool.requestToStr(request, err.getTarget()));
                return;
            }
            if (joinPoint.getArgs()[0] instanceof MethodArgumentTypeMismatchException) {
                log.info(HttpTool.requestToStr(request, null));
                return;
            }
        }
        log.info(HttpTool.requestToStr(request, joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "pointcut()", returning = "res")
    public void afterReturn(Object res) {
        log.info(HttpTool.responseToStr(request, res));
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterReturn(Exception e) throws Exception {
        int code = 0;
        String message = null;
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException err = (MethodArgumentNotValidException) e;
            code = ErrCode.BAD_PARAM.code();
            message = DebugTool.getMessage(err);
        } else if (e instanceof BindException) {
            BindException err = (BindException) e;
            code = ErrCode.BAD_PARAM.code();
            message = DebugTool.getMessage(err);
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException err = (MethodArgumentTypeMismatchException) e;
            code = ErrCode.BAD_PARAM.code();
            message = DebugTool.getMessage(err);
        } else if (e instanceof SrvException) {
            SrvException err = (SrvException) e;
            code = err.getCode();
            message = err.getMessage();
        } else {
            code = ErrCode.SYS_ERROR.code();
            message = e.getMessage();
        }
        log.info("[Response] url: {}, resp: {}", HttpTool.getFullUrl(request), JsonTool.toJson(new RestResponse<>(code, message)));
        throw e;
    }
}
