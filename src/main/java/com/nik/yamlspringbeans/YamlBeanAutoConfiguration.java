package com.nik.yamlspringbeans;

/* Copyright 2017 Nikesh pathak
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 *
 */
@Configuration
public class YamlBeanAutoConfiguration {

    private final Logger log = Logger.getLogger(YamlResourceLoader.class);

    @Autowired
    private ApplicationContext applicationContext;


    @PostConstruct
    public void loadBean()
    {
        try {
            Map<String,Object> beanPropertyMap = new YamlResourceLoader(applicationContext).loadYamlResource();
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
            new BeanDefinationLoader(registry,beanPropertyMap).registerBean();

        }catch(Exception ex)
        {
            log.error(ex);
        }
    }


}
