package com.dhht.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获得spring bean
 * @author admin
 *
 */
public  final class SpringBeanUtil implements ApplicationContextAware {  
    private static ApplicationContext applicationContext = null;  
  
    @Override  
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {  
        if (SpringBeanUtil.applicationContext == null) {  
        	SpringBeanUtil.applicationContext = applicationContext;  
        }  
    }  
  
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
  
    public static Object getBean(String name) {  
        return getApplicationContext().getBean(name);  
    }  
}  