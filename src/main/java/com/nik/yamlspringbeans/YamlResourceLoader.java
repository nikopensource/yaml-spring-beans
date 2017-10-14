package com.nik.yamlspringbeans;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlResourceLoader {

    private Logger log = Logger.getLogger(YamlResourceLoader.class);

    ApplicationContext applicationContext;

    public YamlResourceLoader(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    public Map<String,Object> loadYamlResource() {
        Map<String, Object> listOfBeans = applicationContext.getBeansWithAnnotation(ImportYamlResource.class);
        List<ClassPathResource> listOfResources = new ArrayList<>();
        ImportYamlResource importYamlResource;
        for (String key : listOfBeans.keySet()) {
            importYamlResource = applicationContext.findAnnotationOnBean(key, ImportYamlResource.class);
            for (String item : importYamlResource.value())
            {
                listOfResources.add(new ClassPathResource(item));
            }
        }
        YamlMapFactoryBean factory = new YamlMapFactoryBean();
        factory.setResources(listOfResources.toArray(new ClassPathResource[listOfResources.size()]));
        return factory.getObject();
    }
}
