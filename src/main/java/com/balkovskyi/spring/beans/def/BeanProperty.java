package com.balkovskyi.spring.beans.def;

public class BeanProperty {

    private String name;
    private String beanReference;

    public BeanProperty(String name, String beanReference) {
        this.name = name;
        this.beanReference = beanReference;
    }

    public String getName() {
        return name;
    }

    public String getBeanReference() {
        return beanReference;
    }

    @Override
    public String toString() {
        return "BeanProperty{" +
                "name='" + name + '\'' +
                ", beanReference='" + beanReference + '\'' +
                '}';
    }
}
