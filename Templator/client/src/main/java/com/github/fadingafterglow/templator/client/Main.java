package com.github.fadingafterglow.templator.client;

import com.github.fadingafterglow.templator.core.impl.DefaultWildcardIterator;
import com.github.fadingafterglow.templator.core.impl.DefaultWildcardResolver;
import com.github.fadingafterglow.templator.core.impl.TemplateProcessor;
import com.github.fadingafterglow.templator.core.impl.WildcardModifierProcessorRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Configuration CONFIG = new ConfigurationBuilder()
            .outputDirectory(Path.of("/Users/nick/Downloads"))
            .build();

    public static void main(String[] args) throws IOException {
        WildcardModifierProcessorRegistry.registerModifierProcessors("com.github.fadingafterglow.templator.processors");
        TemplateProcessor templateProcessor = new TemplateProcessor(DefaultWildcardIterator::new, DefaultWildcardResolver::new);

        String template = readFile("/Users/nick/IdeaProjects/JavaAutomatization/Templator/templates/user-info tests (view permission) template.tpl");
        Map<String, String> wildcardValues = new HashMap<>();
        if (CONFIG.getDefaultValues() != null)
            wildcardValues.putAll(CONFIG.getDefaultValues());
        wildcardValues.put("main", "EmployeePosition");
        wildcardValues.put("plural", "EmployeePositions");
        writeFile("Test.java", templateProcessor.process(template, wildcardValues));
    }

    private static String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    private static void writeFile(String filename, String content) throws IOException {
        Path path = CONFIG.getOutputDirectory().resolve(filename);
        Files.deleteIfExists(path);
        Files.writeString(path, content, StandardOpenOption.CREATE);
    }
}
