package com.balkovskyi.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class GetterAndSetterGenerator {
    private Map<String, String> fields = new LinkedHashMap<>();

    public void addField(String fieldName, String fieldType) {
        this.fields.put(fieldName, fieldType);
    }

    private String capitalize(String field) {
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    private void getter(StringBuilder builder, String field, String type) {
        builder.append("\t")
                .append("public ").append(type).append(" get")
                .append(capitalize(field))
                .append("() {")
                .append(System.lineSeparator());
        builder.append("\t")
                .append("\t")
                .append("return this.").append(field).append(";")
                .append(System.lineSeparator())
                .append("\t}");
    }

    private void setter(StringBuilder builder, String field, String type) {
        builder.append("\t")
                .append("public void set").append(capitalize(field))
                .append("(").append(type).append(" ").append(field).append(") {")
                .append(System.lineSeparator());
        builder.append("\t")
                .append("\t")
                .append("this.").append(field).append(" = ").append(field).append(";")
                .append(System.lineSeparator())
                .append("\t}");
    }

    private void getterAndSetter(StringBuilder builder, String field, String type) {
        getter(builder, field, type);
        builder.append(System.lineSeparator()).append(System.lineSeparator());
        setter(builder, field, type);
    }

    public String generateGettersAndSetters() {
        StringBuilder builder = new StringBuilder();
        fields.keySet().forEach(k -> {
            getterAndSetter(builder, k, fields.get(k));
            builder.append(System.lineSeparator())
                    .append(System.lineSeparator());
        });
        return builder.toString();
    }
}
