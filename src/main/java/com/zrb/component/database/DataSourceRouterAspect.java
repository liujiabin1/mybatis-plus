package com.zrb.component.database;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Author: Hardy
 * Date:   2018/8/20 21:11
 * Description: @Order(1) -> 保证 DataSourceRouterAspect 先于 @Transaction
 **/
@Aspect
@Order(1)
@Component
public class DataSourceRouterAspect {

    @Around("@within(router)")
    public Object around1(ProceedingJoinPoint pj, DataSourceRouter router) throws Throwable {
        return handle(pj, router);
    }

    @Around("@annotation(router)")
    public Object around2(ProceedingJoinPoint pj, DataSourceRouter router) throws Throwable {
        return handle(pj, router);
    }

    private Object handle(ProceedingJoinPoint pj, DataSourceRouter router) throws Throwable {
        DataSourceType origType = DataSourceDynamicRouter.getType();
        try {
            DataSourceDynamicRouter.setType(router.value());
            return pj.proceed();
        } finally {
            if (origType == null)
                DataSourceDynamicRouter.clear();
            else
                DataSourceDynamicRouter.setType(origType);
        }
    }
}
