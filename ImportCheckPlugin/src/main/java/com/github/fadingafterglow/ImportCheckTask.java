package com.github.fadingafterglow;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ImportCheckTask extends DefaultTask {

    private static final Pattern PATTERN = Pattern.compile("import ((\\w+\\.)*\\w+)");

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract ConfigurableFileCollection getSourceFiles();

    @TaskAction
    public void check() {
        getSourceFiles().forEach(this::checkFileImports);
    }

    private void checkFileImports(File file) {
        try {
            String content = Files.readString(file.toPath());
            Matcher matcher = PATTERN.matcher(content);
            LinkedHashSet<String> imports = new LinkedHashSet<>();
            imports.add("");
            while (matcher.find()) {
                String currentImport = matcher.group(1);
                if (imports.contains(currentImport))
                    getLogger().warn("Duplicate import '{}' in file {}", currentImport, file.getName());
                String previousImport = imports.getLast();
                if (previousImport.compareTo(currentImport) > 0)
                    getLogger().warn("Out of order imports '{}' and '{}' in file {}", previousImport, currentImport, file.getName());
                imports.add(currentImport);
            }
        }
        catch (IOException exception) {
            throw new GradleException("Cannot read source file: " + file.getName(), exception);
        }
    }
}
