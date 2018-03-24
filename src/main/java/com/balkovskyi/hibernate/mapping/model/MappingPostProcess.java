package com.balkovskyi.hibernate.mapping.model;

import org.w3c.dom.Node;

public interface MappingPostProcess {

    void postProcess(Node node);
}
