package com.balkovskyi.spring.beans.def;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanDefinition {

    private String name;
    private String className;
    private String scope;
    private final List<BeanProperty> properties;

    public BeanDefinition() {
        properties = new ArrayList<>();
    }

    public BeanDefinition(String beanName, String className, String scope) {
        this();
        this.name = beanName;
        this.className = className;
        this.scope = scope;
    }

    public BeanDefinition(String beanName, String className) {
        this(beanName, className, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<BeanProperty> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public void addReference(String fieldName, String referencedBean) {
        properties.add(new BeanProperty(fieldName, referencedBean));
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", scope='" + scope + '\'' +
                ", properties=" + properties +
                '}';
    }
}
