package com.balkovskyi.hibernate;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

public class DirectoryScanner {
    private final String pattern;
    private final File baseDir;

    public DirectoryScanner(File baseDir, String pattern) {
        this.baseDir = baseDir;
        this.pattern = pattern;
    }

    public void scan(Consumer<File> action) {
        listFilesInDirectory(baseDir, action);
    }

    private void listFilesInDirectory(File dir, Consumer<File> action) {
        for(File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                listFilesInDirectory(file, action);
            } else if (file.getName().matches(pattern)) {
                action.accept(file);
            }
        }
    }
}
