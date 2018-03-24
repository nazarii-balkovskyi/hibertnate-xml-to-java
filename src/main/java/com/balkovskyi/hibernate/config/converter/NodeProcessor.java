package com.balkovskyi.hibernate.config.converter;

import com.balkovskyi.hibernate.mapping.model.Mapping;

import org.w3c.dom.Node;

public interface NodeProcessor {
    Mapping processNode(Node node);
}
