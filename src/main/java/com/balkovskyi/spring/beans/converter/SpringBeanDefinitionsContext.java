package com.balkovskyi.spring.beans.converter;

import com.balkovskyi.model.ResultCollector;
import com.balkovskyi.spring.beans.def.BeanDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpringBeanDefinitionsContext implements ResultCollector<BeanDefinition> {
    private final List<BeanDefinition> beanDefinitions;

    public SpringBeanDefinitionsContext() {
        this.beanDefinitions = new ArrayList<>();
    }

    @Override
    public void collect(BeanDefinition result) {
        if (StringUtils.isNoneBlank(result.getName(), result.getClassName())) {
            beanDefinitions.add(result);
        }
    }

    public List<BeanDefinition> getBeanDefinitions() {
        return Collections.unmodifiableList(beanDefinitions);
    }
}
