package com.riverstar.component.database;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Hardy
 * Date:   2018/8/20 21:15
 * Description:
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSourceRouter {
    DataSourceType value() default DataSourceType.SLAVE;
}
