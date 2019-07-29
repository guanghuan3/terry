package com.terry.archer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
@Slf4j
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 获取spring上下文中的bean
     * @param beanId
     * @return
     */
    public static Object getBean(String beanId) {
        try {
            return applicationContext.getBean(beanId);
        } catch (Exception e) {
            log.info("获取Bean失败：没有获取到[{}]", new Object[]{beanId});
            return null;
        }
    }

    /**
     * 获取spring上下文中的bean
     * @param clazz
     * @return
     */
    public static Object getBean(Class<?> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            log.info("获取Bean失败：没有获取到[{}]", new Object[]{clazz});
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }
}
