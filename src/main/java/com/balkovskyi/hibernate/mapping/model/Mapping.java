package com.balkovskyi.hibernate.mapping.model;

import org.w3c.dom.Node;

import java.util.Set;

public interface Mapping {
    String name();
    String type();
    String mappedElement();
    String generateDefinition();
    Set<String> imports();
    void processNode(Node node);
}
