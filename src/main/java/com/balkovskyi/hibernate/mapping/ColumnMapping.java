package com.balkovskyi.hibernate.mapping;

import java.util.Collections;
import java.util.List;

public class ColumnMapping extends BaseMapping {

    public ColumnMapping() {
        importsSet.add("javax.persistence.Column");
    }

    @Override
    protected List<String> getAnnotations() {
        return Collections.singletonList(String.format("@Column(name = \"%s\")", mappedElement()));
    }

    @Override
    protected String getModifier() {
        return "private";
    }

    @Override
    protected String formatAnnotation(String definition) {
        return String.format("\t%s", definition);
    }

    @Override
    protected String formatDefinition(String definition) {
        return String.format("\t%s;", definition);
    }
}
