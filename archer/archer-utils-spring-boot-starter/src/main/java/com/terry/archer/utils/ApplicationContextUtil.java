package com.terry.archer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
@Slf4j
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 获取spring上下文中的bean，根据beanId获取
     * @param beanId
     * @return
     */
    public static Object getBean(String beanId) {
        try {
            return APPLICATION_CONTEXT.getBean(beanId);
        } catch (Exception e) {
            log.info("获取Bean失败：没有获取到[{}]", new Object[]{beanId});
            return null;
        }
    }

    /**
     * 获取spring上下文中的bean，根据类型获取
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        try {
            return APPLICATION_CONTEXT.getBean(clazz);
        } catch (Exception e) {
            log.info("获取Bean失败：没有获取到[{}]", new Object[]{clazz});
            return null;
        }
    }

    /**
     * 根据类型获取该类型下的所有相关实例列表
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        try {
            return APPLICATION_CONTEXT.getBeansOfType(clazz);
        } catch (Exception e) {
            log.info("获取Bean列表失败：没有获取到相关[{}]类型的实例", new Object[]{clazz});
            return null;
        }
    }

    /**
     * spring托管后自动装配context，赋值给静态属性供后续操作
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.APPLICATION_CONTEXT = applicationContext;
    }
}
