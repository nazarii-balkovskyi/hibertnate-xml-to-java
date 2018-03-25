package com.balkovskyi.xml;

import org.w3c.dom.Node;

public interface XmlNodeProcessor<T> {
    T processRootNode(Node rootNode);
    T processChildNodes(Node childNode, Node rootNode);
}
