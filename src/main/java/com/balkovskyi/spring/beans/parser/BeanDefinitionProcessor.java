package com.balkovskyi.spring.beans.parser;

import com.balkovskyi.spring.beans.def.BeanDefinition;
import com.balkovskyi.xml.XmlNodeProcessor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BeanDefinitionProcessor implements XmlNodeProcessor<BeanDefinition> {

    @Override
    public BeanDefinition processRootNode(Node rootNode) {
        return null; // no need to parse root element
    }

    private String getNodeValue(Node node) {
        return node == null ? null : node.getNodeValue().trim();
    }

    private void processProperties(BeanDefinition beanDefinition, Node propertyNode) {
        NamedNodeMap properties = propertyNode.getAttributes();
        beanDefinition.addReference(getNodeValue(properties.getNamedItem("name")), getNodeValue(properties.getNamedItem("ref")));
    }

    private void processBeanProperties(BeanDefinition beanDefinition, Node beanNode) {
        NodeList childNodes = beanNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(childNode.getNodeName())) {
                processProperties(beanDefinition, childNode);
            }
        }
    }

    @Override
    public BeanDefinition processChildNodes(Node childNode, Node rootNode) {
        NamedNodeMap attributes = childNode.getAttributes();
        String name = getNodeValue(attributes.getNamedItem("id"));
        name = name == null ? getNodeValue(attributes.getNamedItem("name")) : name;

        BeanDefinition beanDefinition = new BeanDefinition(name, getNodeValue(attributes.getNamedItem("class")),
                getNodeValue(attributes.getNamedItem("scope")));
        processBeanProperties(beanDefinition, childNode);
        return beanDefinition;
    }
}
