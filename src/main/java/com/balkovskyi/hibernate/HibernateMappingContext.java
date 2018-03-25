package com.balkovskyi.hibernate;

import com.balkovskyi.hibernate.mapping.HibernateEntityMapping;
import com.balkovskyi.hibernate.mapping.model.Mapping;
import com.balkovskyi.model.ResultCollector;
import com.balkovskyi.util.GetterAndSetterGenerator;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class HibernateMappingContext implements ResultCollector<Mapping> {
    private final String outputFileSuffix;
    private String packageName;
    private Set<String> imports;
    private List<Mapping> mappings;
    private String outputDestination;
    private GetterAndSetterGenerator getterAndSetterGenerator;

    public HibernateMappingContext() {
        this("");
    }

    public HibernateMappingContext(String outputFileSuffix) {
        imports = new TreeSet<>();
        mappings = new ArrayList<>();
        getterAndSetterGenerator = new GetterAndSetterGenerator();
        this.outputFileSuffix = StringUtils.isNoneBlank(outputFileSuffix) ? "_" + outputFileSuffix : "";
    }

    @Override
    public void collect(Mapping mapping) {
        this.mappings.add(mapping);
        if (mapping instanceof HibernateEntityMapping) {
            String classPackage = ((HibernateEntityMapping) mapping).getPackage();
            this.packageName = classPackage;
            this.outputDestination = String.format("%s/%s%s.java",
                    classPackage.replaceAll("\\.", "/"),
                    mapping.name(),
                    outputFileSuffix);
        } else {
            this.getterAndSetterGenerator.addField(mapping.name(), mapping.type());
        }
        if (mapping.imports() != null && !mapping.imports().isEmpty()) {
            this.imports.addAll(mapping.imports());
        }
    }

    public String getOutputDestination() {
        return outputDestination;
    }

    public String generateCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(String.format("package %s;", packageName))
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        int dotNumberInPackageDef = StringUtils.countMatches(packageName, ".");
        imports
                .stream()
                .filter(imp -> !imp.startsWith(packageName) || StringUtils.countMatches(imp, ".") != dotNumberInPackageDef)
                .forEach(i -> stringBuilder.append(String.format("import %s;", i)).append(System.lineSeparator()));
        mappings.forEach(m -> stringBuilder.append(m.generateDefinition()).append(System.lineSeparator()));

        stringBuilder.append(System.lineSeparator())
                    .append(getterAndSetterGenerator.generateGettersAndSetters())
                    .append(System.lineSeparator()).append("}");
        return stringBuilder.toString();
    }
}
