package com.balkovskyi.spring.beans.converter;

import com.balkovskyi.spring.beans.def.BeanDefinition;
import com.balkovskyi.spring.beans.def.BeanProperty;
import com.balkovskyi.spring.beans.parser.BeanDefinitionProcessor;
import com.balkovskyi.xml.XmlFileConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpringBeansConverter extends XmlFileConverter<BeanDefinition> {

    private final SpringBeanDefinitionsContext context;
    private final List<File> scanDirs;

    public SpringBeansConverter(List<File> directoriesWithSources) {
        super("beans", new BeanDefinitionProcessor());
        this.context = new SpringBeanDefinitionsContext();
        this.scanDirs = directoriesWithSources;
    }

    private Map.Entry<BeanDefinition, File> mapDefinitionToFile(BeanDefinition definition) {
        String beanClassPath = String.format("%s.java", definition.getClassName().replaceAll("\\.", "/"));
        Map.Entry<BeanDefinition, File> entry = null;
        for (File dir : scanDirs) {
            File javaFile = new File(dir, beanClassPath);
            if (javaFile.exists()) {
                entry = new AbstractMap.SimpleEntry<>(definition, javaFile);
                break;
            }
        }
        if (entry == null) {
            System.err.println(String.format("[WARNING] Can not map bean definition '%s' to java file.", definition.getName()));
        }
        return entry;
    }

    private Matcher getDefinitionReplaceString(BeanDefinition beanDefinition) {
        String className = StringUtils.substringAfterLast(beanDefinition.getClassName(), ".");
        return Pattern.compile(String.format("(public\\s*.*\\s*class\\s*%s)", className)).matcher("");
    }

    private Matcher getDefinitionPropertyReplaceString(BeanProperty property) {
        return Pattern.compile(String.format("(private\\s*.*\\s*%s;)", property.getName())).matcher("");
    }

    private String getAnnotationsDef(BeanProperty property) {
        return String.format("\t@Autowired\n\t@Qualifier(\"%s\")", property.getBeanReference());
    }

    private void processDefinition(Map.Entry<BeanDefinition, File> beanDefinitionFileEntry) {
        BeanDefinition definition = beanDefinitionFileEntry.getKey();
        File targetJavaFile = beanDefinitionFileEntry.getValue();
        System.out.println(String.format("[PROCESSING] Mapped %s to file '%s'", definition.getName(), targetJavaFile.getAbsolutePath()));
        try {
            String classContent = FileUtils.readFileToString(targetJavaFile, Charset.defaultCharset());
            classContent = appendComponentDefinition(definition, classContent);
            classContent = processProperties(definition, classContent);
            FileWriter fileWriter = new FileWriter(targetJavaFile);
            fileWriter.write(classContent);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.err.println(String.format("[ERROR] Can not process bean %s. Reason: '%s'.", definition.getName(), e.getMessage()));
        }
    }

    private String processProperties(BeanDefinition definition, String classContent) {
        for ( BeanProperty property : definition.getProperties()) {
            Matcher matcher = getDefinitionPropertyReplaceString(property).reset(classContent);
            if (matcher.find()) {
                StringBuilder builder = new StringBuilder()
                        .append("@Autowired")
                        .append(System.lineSeparator())
                        .append("\t@Qualifier(\"").append(property.getBeanReference()).append("\")")
                        .append(System.lineSeparator())
                        .append("\t").append(matcher.group());
                classContent = classContent.replace(matcher.group(), builder.toString());
            } else {
                System.out.println(String.format("\t\t(WARNING) Bean reference ('%s') field '%s' was not found.",
                        property.getBeanReference(), property.getName()));
            }
        }
        return classContent;
    }

    private String appendComponentDefinition(BeanDefinition definition, String classContent) {
        Matcher matcher = getDefinitionReplaceString(definition).reset(classContent);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Can not find class definition. TODO");
        }
        String classReplaceStr = matcher.group();
        StringBuilder imports = new StringBuilder()
                .append("import org.springframework.stereotype.Component;");
        StringBuilder replace = new StringBuilder()
                .append("@Component(\"").append(definition.getName()).append("\")")
                .append(System.lineSeparator());
        if (definition.getScope() != null) {
            imports
                    .append(System.lineSeparator())
                    .append("import org.springframework.context.annotation.Scope;")
                    .append(System.lineSeparator())
                    .append("import org.springframework.beans.factory.config.ConfigurableBeanFactory;");
            replace
                    .append("@Scope(ConfigurableBeanFactory.SCOPE_")
                    .append(definition.getScope().equals("prototype") ? "PROTOTYPE" : "SINGLETON")
                    .append(")").append(System.lineSeparator());
        }
        if (definition.getProperties().size() > 0) {
            imports.append(System.lineSeparator())
                    .append("import org.springframework.beans.factory.annotation.Autowired;").append(System.lineSeparator())
                    .append("import org.springframework.beans.factory.annotation.Qualifier;").append(System.lineSeparator());
        }
        imports
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(replace)
                .append(classReplaceStr);
        return classContent.replace(classReplaceStr, imports.toString());
    }

    @Override
    protected void scanCompleted(File baseDirectory) {
        System.out.println("== [SCAN COMPLETED] ==");
        context.getBeanDefinitions()
                .stream()
                .map(this::mapDefinitionToFile)
                .filter(Objects::nonNull)
                .forEach(this::processDefinition);
    }

    @Override
    public void convert(File file) {
        try {
            System.out.println(String.format("Processing file ['%s'].", file));
            parseXml(file, context);
        } catch (Exception e) {
            System.err.println(String.format("> Unable to parse beans file ['%s'].", file.getAbsolutePath()));
        }
    }
}
