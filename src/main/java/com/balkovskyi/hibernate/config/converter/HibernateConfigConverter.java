package com.balkovskyi.hibernate.config.converter;

import com.balkovskyi.hibernate.HibernateMappingContext;
import com.balkovskyi.hibernate.mapping.model.Mapping;
import com.balkovskyi.xml.XmlFileConverter;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class HibernateConfigConverter extends XmlFileConverter<Mapping> {

    private final File outputBaseDir;

    public HibernateConfigConverter(File outputBaseDir) {
        super("class", new HibernateConfigNodeProcessor());
        this.outputBaseDir = outputBaseDir;
    }

    @Override
    protected void scanCompleted(File baseDirectory) {
        System.out.println("(!) Finished scanning of directory.");
    }

    @Override
    public void convert(File file) {
        System.out.println(String.format("Processing file: %s", file.getAbsolutePath()));
        try {
            HibernateMappingContext hibernateMappingContext = new HibernateMappingContext();
            parseXml(file, hibernateMappingContext);
            File outputFile = new File(Paths.get(outputBaseDir.getAbsolutePath(), hibernateMappingContext.getOutputDestination()).toString());
            if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs() || !outputFile.getParentFile().isDirectory()) {
                throw new IllegalArgumentException("Unable to create output directory or output path is not a directory.");
            }
            System.out.println(String.format("> Saving result to '%s'", outputFile.getAbsolutePath()));
            try (FileWriter fileWriter = new FileWriter(Paths.get(outputBaseDir.getAbsolutePath(), hibernateMappingContext.getOutputDestination()).toString());) {
                fileWriter.write(hibernateMappingContext.generateCode());
                fileWriter.flush();
            }
        } catch (Exception e) {
            System.err.println(String.format("> Can not parse hibernate mapping file. Reason: '%s'", e.getMessage()));
        }
    }
}
