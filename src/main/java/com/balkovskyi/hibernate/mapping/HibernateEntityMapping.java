package com.balkovskyi.hibernate.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HibernateEntityMapping extends BaseMapping {
    private String packageName;

    public HibernateEntityMapping() {
        importsSet.add("javax.persistence.Entity");
        importsSet.add("javax.persistence.Table");
    }

    @Override
    protected void setName(String name) {
        List<String> nameDef = new ArrayList<>(Arrays.asList(name.split("\\.")));
        super.setName(nameDef.get(nameDef.size() - 1));
        nameDef.remove(nameDef.size() - 1);
        this.packageName = nameDef.stream().collect(Collectors.joining("."));
    }

    public String getPackage() {
        return this.packageName;
    }

    @Override
    public String type() {
        return "class";
    }

    @Override
    protected List<String> getAnnotations() {
        return Arrays.asList("@Entity", String.format("@Table(name = \"%s\")", mappedElement()));
    }

    @Override
    protected String getModifier() {
        return "public";
    }

    @Override
    protected String formatAnnotation(String annotation) {
        return annotation;
    }

    @Override
    protected String formatDefinition(String definition) {
        return String.format("%s {", definition);
    }

    @Override
    protected String typeAttribute() {
        return "class";
    }

    @Override
    protected String mappedElementAttribute() {
        return "table";
    }
}
