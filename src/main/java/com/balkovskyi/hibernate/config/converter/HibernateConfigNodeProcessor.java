package com.balkovskyi.hibernate.config.converter;

import com.balkovskyi.hibernate.mapping.*;

import com.balkovskyi.hibernate.mapping.model.Mapping;
import com.balkovskyi.xml.XmlNodeProcessor;
import org.w3c.dom.Node;

public class HibernateConfigNodeProcessor implements XmlNodeProcessor<Mapping> {

    private static final String ID_MAPPING_NODE_NAME = "id";
    private static final String COLUMN_MAPPING_NODE_NAME = "property";
    private static final String MANY_TO_ONE_MAPPING_NODE_NAME = "many-to-one";
    private static final String ONE_TO_MANY_SET_MAPPING_NODE_NAME = "set";
    private static final String ONE_TO_MANY_LIST_MAPPING_NODE_NAME = "list";
    private static final String ONE_TO_ONE_MAPPING_NODE_NAME = "one-to-one";

    @Override
    public Mapping processRootNode(Node node) {
        HibernateEntityMapping result = new HibernateEntityMapping();
        result.processNode(node);
        return result;
    }

    @Override
    public Mapping processChildNodes(Node childNode, Node rootNode) {
        String nodeName = childNode.getNodeName();
        Mapping result;
        switch (nodeName) {
            case ID_MAPPING_NODE_NAME:
                result = new IdMapping();
                break;
            case COLUMN_MAPPING_NODE_NAME:
                result = new ColumnMapping();
                break;
            case MANY_TO_ONE_MAPPING_NODE_NAME:
                result = new ManyToOneMapping();
                break;
            case ONE_TO_MANY_SET_MAPPING_NODE_NAME:
            case ONE_TO_MANY_LIST_MAPPING_NODE_NAME:
                result = new OneToManyMapping();
                break;
            case ONE_TO_ONE_MAPPING_NODE_NAME:
                result = new OneToOneMapping();
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid or unknown node name '%s'", nodeName));
        }
        result.processNode(childNode);
        return result;
    }
}
