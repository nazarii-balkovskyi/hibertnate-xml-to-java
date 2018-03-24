package com.balkovskyi.hibernate.mapping;

import java.util.ArrayList;
import java.util.List;

public class IdMapping extends ColumnMapping {

    public IdMapping() {
        super();
        importsSet.add("javax.persistence.Id");
        importsSet.add("javax.persistence.GeneratedValue");
        importsSet.add("javax.persistence.GenerationType");
    }

    @Override
    protected List<String> getAnnotations() {
        List<String> annotations = new ArrayList<>();
        annotations.add("@Id");
        annotations.add("@GeneratedValue(strategy = GenerationType.AUTO)");
        annotations.addAll(super.getAnnotations());
        return annotations;
    }
}
