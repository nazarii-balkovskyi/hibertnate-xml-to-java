package com.balkovskyi.xml;

import com.balkovskyi.util.DirectoryScanner;
import com.balkovskyi.model.ResultCollector;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public abstract class XmlFileConverter<T> {

    private final String rootNodeName;
    private final XmlNodeProcessor<T> nodeProcessor;

    protected XmlFileConverter(String rootNodeName, XmlNodeProcessor<T> nodeProcessor) {
        this.rootNodeName = rootNodeName;
        this.nodeProcessor = nodeProcessor;
    }

    protected void parseXml(File file, ResultCollector<T> resultCollector) throws IOException {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            Node rootNode = document.getDocumentElement();
            if (!rootNode.getNodeName().equals(this.rootNodeName)) {
                NodeList childNodes = rootNode.getChildNodes();
                for (int i =  0; i < childNodes.getLength(); i++) {
                    Node item = childNodes.item(i);
                    if (item.getNodeType() == Node.ELEMENT_NODE && rootNodeName.equals(item.getNodeName())) {
                        rootNode = item;
                    }
                }
                if (rootNode == document.getDocumentElement()) {
                    throw new IllegalArgumentException(String.format("Input file '%s' doesn't contain specified root node.", file.getAbsolutePath()));
                }
            }
            T result = nodeProcessor.processRootNode(rootNode);
            if (result != null) {
                resultCollector.collect(result);
            }
            processChildNodes(rootNode, resultCollector);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalArgumentException("Unable to parse specified XML document.", e);
        }
    }

    private void processChildNodes(Node rootNode, ResultCollector<T> resultCollector) {
        NodeList childNodes = rootNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node currentNode = childNodes.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                T result = nodeProcessor.processChildNodes(currentNode, rootNode);
                if (result != null) {
                    resultCollector.collect(result);
                }
            }
        }
    }

    protected abstract void scanCompleted(File baseDirectory);
    public abstract void convert(File file);
    public final void convert(File baseDirectory, String configFilePattern) {
        DirectoryScanner directoryScanner = new DirectoryScanner(baseDirectory, configFilePattern);
        directoryScanner.scan(this::convert);
        scanCompleted(baseDirectory);
    }
}
