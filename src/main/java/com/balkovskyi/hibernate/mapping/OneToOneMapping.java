package com.balkovskyi.hibernate.mapping;

public class OneToOneMapping extends ColumnMapping {

    public OneToOneMapping() {
        importsSet.add("javax.persistence.OneToOne");
    }
}
