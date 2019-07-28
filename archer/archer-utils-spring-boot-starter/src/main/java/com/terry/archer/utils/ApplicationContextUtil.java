package com.terry.archer.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 获取spring上下文中的bean
     * @param beanId
     * @return
     */
    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }

    /**
     * 获取spring上下文中的bean
     * @param clazz
     * @return
     */
    public static Object getBean(Class<?> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }
}
