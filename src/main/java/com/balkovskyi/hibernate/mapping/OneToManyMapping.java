package com.balkovskyi.hibernate.mapping;

import com.balkovskyi.hibernate.mapping.model.MappingPostProcess;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collections;
import java.util.List;

public class OneToManyMapping extends RelationMapping implements MappingPostProcess {
    private String collectionType;

    public OneToManyMapping() {
        importsSet.add("javax.persistence.OneToMany");
    }

    @Override
    protected List<String> getAnnotations() {
        return Collections.singletonList(String.format("@OneToMany(%s)", getCascade(false)));
    }

    @Override
    public String type() {
        String collectionType = this.collectionType.equals("set") ? "Set" : "List";
        importsSet.add(String.format("java.util.%s", collectionType));
        return String.format("%s<%s>", collectionType, super.type());
    }

    @Override
    public void postProcess(Node node) {
        collectionType = node.getNodeName();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node currentNode = childNodes.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if ("key".equals(currentNode.getNodeName())) {
                    processNodeAttributes(currentNode, item -> {
                        if ("column".equals(item.getNodeName())) {
                            setMappedElement(item.getNodeValue());
                        }
                    });
                } else if ("one-to-many".equals(currentNode.getNodeName())) {
                    processNodeAttributes(currentNode, item -> {
                        if ("class".equals(item.getNodeName())) {
                            setType(item.getNodeValue());
                        }
                    });
                }
            }
        }
    }
}
