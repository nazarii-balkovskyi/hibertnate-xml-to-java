package com.balkovskyi.hibernate.mapping;

import com.balkovskyi.hibernate.mapping.model.Mapping;
import com.balkovskyi.hibernate.mapping.model.MappingPostProcess;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public abstract class BaseMapping implements Mapping {

    private String name;
    private String type;
    private String mappedElement;
    protected final Set<String> importsSet = new LinkedHashSet<>();

    @Override
    public String name() {
        return name;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String mappedElement() {
        return mappedElement;
    }

    @Override
    public Set<String> imports() {
        return importsSet;
    }

    @Override
    public String generateDefinition() {
        StringBuilder stringBuilder = new StringBuilder().append(System.lineSeparator());
        List<String> annotations = getAnnotations();
        if (annotations != null && !annotations.isEmpty()) {
            annotations.forEach(a -> stringBuilder.append(formatAnnotation(a)).append(System.lineSeparator()));
        }
        stringBuilder.append(formatDefinition(getDefinition()));
        return stringBuilder.toString();
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setType(String type) {
        String[] typeParts = type.split("\\.");
        this.type = typeParts[typeParts.length - 1];
        this.importsSet.add(type);
    }

    protected void setMappedElement(String mappedElement) {
        this.mappedElement = mappedElement;
    }

    private String getDefinition() {
        return String.format("%s %s %s", getModifier(), type(), name());
    }

    protected String typeAttribute() {
        return "type";
    }

    protected String mappedElementAttribute() {
        return "column";
    }

    protected String nameAttribute() {
        return "name";
    }

    protected abstract List<String> getAnnotations();
    protected abstract String getModifier();
    protected abstract String formatAnnotation(String definition);
    protected abstract String formatDefinition(String definition);

    protected void processNodeAttributes(Node node, Consumer<Node> action) {
        NamedNodeMap attributes = node.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            if (item.getNodeType() == Node.ATTRIBUTE_NODE) {
                action.accept(item);
            }
        }
    }

    protected void handleUnknownAttribute(String attrName, String value) {
        // NO OP
    }

    @Override
    public void process(Node node) {
        processNodeAttributes(node, item -> {
            String elementName = item.getNodeName();
            String nodeValue = item.getNodeValue();
            if (this.typeAttribute().equals(elementName)) {
                setType(nodeValue);
            } else if (this.mappedElementAttribute().equals(elementName)) {
                setMappedElement(nodeValue);
            } else if (this.nameAttribute().equals(elementName)) {
                setName(nodeValue);
            } else {
                handleUnknownAttribute(elementName, nodeValue);
            }
        });
        if (this instanceof MappingPostProcess) {
            ((MappingPostProcess)this).postProcess(node);
        }
    }
}
