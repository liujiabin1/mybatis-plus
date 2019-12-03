package com.zrb.component.database;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Author: Hardy
 * Date:   2019/12/3
 * Description:
 **/
public class DataSourceAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware, Ordered {
    private Advice advice;

    private Pointcut pointcut;

    public DataSourceAdvisor(Advice advice) {
        this.advice = advice;
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

    // 定义切入点，就是添加了@Ds注解的类和方法
    private Pointcut buildPointcut() {
        Pointcut cpc = new AnnotationMatchingPointcut(DataSourceRouter.class, true);
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(DataSourceRouter.class);
        return new ComposablePointcut(cpc).union(mpc);
    }
}
