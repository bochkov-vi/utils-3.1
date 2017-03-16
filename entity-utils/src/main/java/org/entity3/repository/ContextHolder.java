package org.entity3.repository;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by bochkov on 16.03.17.
 */
public class ContextHolder implements ApplicationContextAware {
    public static ApplicationContext CONTEXT;

   
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }
}
