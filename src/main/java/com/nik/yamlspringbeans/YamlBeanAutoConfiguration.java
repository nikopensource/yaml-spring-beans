package com.nik.yamlspringbeans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
public class YamlBeanAutoConfiguration {


    @Autowired
    ApplicationContext applicationContext;


    @PostConstruct
    public void loadBean()
    {
        try {
            Map<String,Object> beanPropertyMap = new YamlResourceLoader(applicationContext).loadYamlResource();
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
            new BeanDefinationLoader(registry,beanPropertyMap).registerBean();

        }catch(Exception ex)
        {
          System.out.println(ex.getMessage());
        }
    }


}
