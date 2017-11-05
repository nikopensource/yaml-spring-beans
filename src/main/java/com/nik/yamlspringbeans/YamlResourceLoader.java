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
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Read yaml file from resources and set into YamlMapFactoryBean
 */
public class YamlResourceLoader {

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
