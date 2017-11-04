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

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanDefinationLoader {

    BeanDefinitionRegistry registry;

    Map<String,Object> beanPropertyMap;

    public BeanDefinationLoader(BeanDefinitionRegistry registry, Map<String,Object> beanPropertyMap)
    {
        this.registry = registry;
        this.beanPropertyMap = beanPropertyMap;
    }

    public void loadBeanDefination(String beanName,Map<String,Object> objectMap)
    {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        try
        {
            for(String item: objectMap.keySet())
            {
                switch (item)
                {
                    case "class":
                        beanDefinition.setBeanClass(Class.forName((String) objectMap.get(item)));
                        break;
                    case "lazy-init":
                        beanDefinition.setLazyInit((Boolean) objectMap.get(item));
                        break;
                    case "init-method":
                        beanDefinition.setInitMethodName((String) objectMap.get(item));
                        break;
                    case "destory-method":
                        beanDefinition.setDestroyMethodName((String) objectMap.get(item));
                        break;
                    case "scope":
                        beanDefinition.setScope((String) objectMap.get(item));
                        break;
                    case "properties":
                        beanDefinition.setPropertyValues(loadMutablePropertyValues((Map<String, Object>) objectMap.get(item)));
                        break;
                }
            }
            beanDefinition.setAutowireCandidate(true);
        }catch(ClassNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
        if(!registry.isBeanNameInUse(beanName))
            registry.registerBeanDefinition(beanName,beanDefinition);
    }

    public MutablePropertyValues loadMutablePropertyValues(Map<String,Object> objectMap)
    {
        return new MutablePropertyValues(loadPropertyValue(objectMap));
    }

    public List<PropertyValue> loadPropertyValue(Map<String,Object> objectMap)
    {
        List<PropertyValue> propertyValueList = new ArrayList<>();
        for(String item: objectMap.keySet())
        {
            if(objectMap.get(item) instanceof String &&  ((String) objectMap.get(item)).startsWith("ref::"))
            {
                String value = (String) objectMap.get(item);
                value = value.replace("ref::","").trim();
                if(registry.isBeanNameInUse(value)) {
                    propertyValueList.add(new PropertyValue(item,new RuntimeBeanReference(value)));
                }else
                {
                    loadBeanDefination(value, (Map<String, Object>) beanPropertyMap.get(value));
                    propertyValueList.add(new PropertyValue(item,new RuntimeBeanReference(value)));
                }
            }
            else
            {
                propertyValueList.add(new PropertyValue(item,objectMap.get(item)));
            }
        }
        return propertyValueList;
    }

    public void registerBean()
    {
        for(String item  : beanPropertyMap.keySet())
        {
            loadBeanDefination(item,(Map<String, Object>) beanPropertyMap.get(item));
        }
    }
}
