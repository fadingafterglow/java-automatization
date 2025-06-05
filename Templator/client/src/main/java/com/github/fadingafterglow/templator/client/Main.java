package com.github.fadingafterglow.templator.client;

import com.github.fadingafterglow.templator.core.impl.TemplateProcessor;
import com.github.fadingafterglow.templator.core.impl.WildcardModifierProcessorRegistry;
import com.github.fadingafterglow.templator.core.impl.DefaultWildcardIterator;
import com.github.fadingafterglow.templator.core.impl.DefaultWildcardResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        WildcardModifierProcessorRegistry.registerModifierProcessors("com.github.fadingafterglow.templator.processors");
        TemplateProcessor templateProcessor = new TemplateProcessor(DefaultWildcardIterator::new, DefaultWildcardResolver::new);

        String template = readFile("templates/user-info tests (view permission) template.tpl");
        Map<String, String> wildcardValues = Map.of(
                "main", "EmployeePosition",
                "plural", "EmployeePositions");
        writeFile("/Users/nick/Downloads/Test.java", templateProcessor.process(template, wildcardValues));
    }

    private static String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    private static void writeFile(String filePath, String content) throws IOException {
        Path path = Path.of(filePath);
        Files.deleteIfExists(path);
        Files.writeString(path, content, StandardOpenOption.CREATE);
    }
}
