package com.riverstar.component.database;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author: Hardy
 * Date:   2019/12/3
 * Description:
 **/
public class DataSourceInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DataSourceType origType = DataSourceDynamicRouter.getType();
        try {
            DataSourceDynamicRouter.setType(getType(invocation));
            return invocation.proceed();
        } finally {
            if (origType == null)
                DataSourceDynamicRouter.clear();
            else
                DataSourceDynamicRouter.setType(origType);
        }
    }

    private DataSourceType getType(MethodInvocation invocation) {
        Method method = invocation.getMethod();

        DataSourceRouter router = null;
        if (method.isAnnotationPresent(DataSourceRouter.class)) {
            router = method.getAnnotation(DataSourceRouter.class);
        } else {
            Class<?> targetClass = getTargetClass(invocation);
            if (targetClass != null)
                router = AnnotationUtils.findAnnotation(targetClass, DataSourceRouter.class);
        }
        return router == null ? DataSourceType.MASTER : router.value();
    }

    private Class<?> getTargetClass(MethodInvocation invocation) {
        Object target = invocation.getThis();
        Class<?> targetClass = target.getClass();

        if (Proxy.isProxyClass(targetClass)) {
            // 获取 动态的代理类
            InvocationHandler handler = Proxy.getInvocationHandler(target);

            // 获取 真实被代理的类
            return getMapper(handler);
        } else {
            return invocation.getMethod().getDeclaringClass();
        }
    }

    private Class<?> getMapper(InvocationHandler handler) {
        try {
            Field[] fields = handler.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                Object o = field.get(handler);

                // mapper 本身是 class
                if (!(o instanceof Class)) continue;

                Class<?> mapperClass = (Class) (o);

                // 包含@Mapper
                Mapper anno = mapperClass.getAnnotation(Mapper.class);

                if (anno != null) return mapperClass;
            }
            return null;
        } catch (Exception e) {
        }
        return null;
    }
}
