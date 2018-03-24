package com.balkovskyi.hibernate.config.converter;

import com.balkovskyi.hibernate.mapping.*;

import com.balkovskyi.hibernate.mapping.model.Mapping;
import org.w3c.dom.Node;

public class ConfigNodeProcessor implements NodeProcessor {

    private static final String ID_MAPPING_NODE_NAME = "id";
    private static final String COLUMN_MAPPING_NODE_NAME = "property";
    private static final String CLASS_MAPPING_NODE_NAME = "class";
    private static final String MANY_TO_ONE_MAPPING_NODE_NAME = "many-to-one";
    private static final String ONE_TO_MANY_SET_MAPPING_NODE_NAME = "set";
    private static final String ONE_TO_MANY_LIST_MAPPING_NODE_NAME = "list";
    private static final String ONE_TO_ONE_MAPPING_NODE_NAME = "one-to-one";

    public Mapping processNode(Node node) {
        String nodeName = node.getNodeName();
        Mapping result;
        switch (nodeName) {
            case ID_MAPPING_NODE_NAME:
                result = new IdMapping();
                break;
            case COLUMN_MAPPING_NODE_NAME:
                result = new ColumnMapping();
                break;
            case CLASS_MAPPING_NODE_NAME:
                result = new HibernateEntityMapping();
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
        result.process(node);
        return result;
    }
}
