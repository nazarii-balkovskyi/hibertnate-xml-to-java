package com.balkovskyi.hibernate.mapping;

import com.balkovskyi.hibernate.mapping.model.MappingPostProcess;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;

public class ManyToOneMapping extends RelationMapping implements MappingPostProcess {

    public ManyToOneMapping() {
        super();
        importsSet.add("javax.persistence.ManyToOne");
        importsSet.add("javax.persistence.JoinColumn");
    }

    @Override
    protected List<String> getAnnotations() {
        return Arrays.asList(
                String.format("@ManyToOne(fetch = %s%s)", getFetchType(), getCascade(true)),
                String.format("@JoinColumn(name = \"%s\")", mappedElement())
        );
    }

    @Override
    protected String typeAttribute() {
        return "class";
    }

    @Override
    public void postProcess(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node currentNode = childNodes.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE && "column".equals(currentNode.getNodeName())) {
                processNodeAttributes(currentNode, item -> {
                    if (nameAttribute().equals(item.getNodeName())) {
                        setMappedElement(item.getNodeValue());
                    }
                });
            }
        }
    }
}
