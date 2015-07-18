package com.searchengine.webportal.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext curApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        curApplicationContext = applicationContext;
    }

    public static Object getBean(String name)
    {
        return curApplicationContext.getBean(name);
    }
}
