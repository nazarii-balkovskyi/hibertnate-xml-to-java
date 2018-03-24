package com.balkovskyi.hibernate.mapping;

import java.util.HashMap;
import java.util.Map;

public abstract class RelationMapping extends ColumnMapping {
    private final static Map<String, String> cascadeDefMapping = new HashMap<>();

    static {
        cascadeDefMapping.put("all", "cascade = CascadeType.ALL");
        cascadeDefMapping.put("delete", "cascade = CascadeType.DELETE");
        cascadeDefMapping.put("detach", "cascade = CascadeType.DETACH");
        cascadeDefMapping.put("lock", "cascade = CascadeType.LOCK");
        cascadeDefMapping.put("merge", "cascade = CascadeType.MERGE");
        cascadeDefMapping.put("persist", "cascade = CascadeType.PERSIST");
        cascadeDefMapping.put("remove", "cascade = CascadeType.REMOVE");
        cascadeDefMapping.put("refresh", "cascade = CascadeType.REFRESH");
        cascadeDefMapping.put("all-delete-orphan", "orphanRemoval = true");
    }
    private String cascade;
    private boolean lazy;

    @Override
    protected void handleUnknownAttribute(String attrName, String value) {
        switch (attrName) {
            case "cascade":
                cascade = value;
                break;
            case "lazy":
                lazy = Boolean.valueOf(value);
                break;
        }
    }

    String getCascade(boolean includeLeadingComma) {
        if (cascade == null || cascade.isEmpty()) {
            return "";
        }
        importsSet.add("javax.persistence.CascadeType");
        String cascadeValue = cascadeDefMapping.get(cascade);
        boolean isCascadeValueEmpty = cascadeValue == null || cascadeValue.isEmpty();
        return String.format("%s%s", !isCascadeValueEmpty && includeLeadingComma ? ", " : "", cascadeValue);
    }

    String getFetchType() {
        importsSet.add("javax.persistence.FetchType");
        return String.format("FetchType.%s", lazy ? "LAZY" : "EAGER");
    }
}
