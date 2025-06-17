package com.github.fadingafterglow.templator.client;

import com.github.fadingafterglow.builder.annotations.Builder;

import java.nio.file.Path;
import java.util.Map;

@Builder
public class Configuration {
    private Path outputDirectory;
    private Map<String, String> defaultValues;

    public Path getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Map<String, String> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(Map<String, String> defaultValues) {
        this.defaultValues = defaultValues;
    }
}
