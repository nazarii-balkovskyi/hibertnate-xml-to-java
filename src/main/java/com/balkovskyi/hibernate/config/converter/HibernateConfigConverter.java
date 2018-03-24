package com.balkovskyi.hibernate.config.converter;

import com.balkovskyi.hibernate.DirectoryScanner;
import com.balkovskyi.hibernate.HibernateMappingContext;
import com.balkovskyi.hibernate.mapping.model.Mapping;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class HibernateConfigConverter {

    private final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    private final File outputBaseDir;

    public HibernateConfigConverter(File outputBaseDir) {
        this.outputBaseDir = outputBaseDir;
    }

    public void convert(File file) {
        System.out.println(String.format("Processing file: %s", file.getAbsolutePath()));
        try {
            DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            if (!document.getDocumentElement().getTagName().equals("hibernate-mapping")) {
                throw new IllegalArgumentException("Invalid XML configuration file. ");
            }
            ConfigNodeProcessor nodeProcessor = new ConfigNodeProcessor();
            Node classNode = document.getDocumentElement().getChildNodes().item(1);
            if (!classNode.getNodeName().equals("class")) {
                throw new IllegalArgumentException("There is no class element");
            }

            HibernateMappingContext hibernateElement = new HibernateMappingContext();
            Mapping entityMapping = nodeProcessor.processNode(classNode);
            hibernateElement.addMapping(entityMapping);
            System.out.println(String.format("> Entity found '%s'. Class name is '%s'", entityMapping.mappedElement(), entityMapping.name()));
            NodeList classChildNodes = classNode.getChildNodes();
            for (int i = 0; i < classChildNodes.getLength(); i++) {
                Node item = classChildNodes.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    hibernateElement.addMapping(nodeProcessor.processNode(item));
                }
            }
            File outputFile = new File(Paths.get(outputBaseDir.getAbsolutePath(), hibernateElement.getOutputDestination()).toString());
            if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs() || !outputFile.getParentFile().isDirectory()) {
                throw new IllegalArgumentException("Unable to create output directory or output path is not a directory.");
            }
            System.out.println(String.format("> Saving result to '%s'", outputFile.getAbsolutePath()));
            try (FileWriter fileWriter = new FileWriter(Paths.get(outputBaseDir.getAbsolutePath(), hibernateElement.getOutputDestination()).toString());) {
                fileWriter.write(hibernateElement.generateCode());
                fileWriter.flush();
            }
        } catch (Exception e) {
            System.err.println(String.format("> Can not parse hibernate mapping file. Reason: '%s'", e.getMessage()));
        }
    }

    public void convert(File baseFile, String configFilePattern) {
        DirectoryScanner directoryScanner = new DirectoryScanner(baseFile, configFilePattern);
        directoryScanner.scan(this::convert);
    }
}
