package com.github.fadingafterglow.templator.client;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.util.Map;

@Data
@Builder
public class Configuration {
    private Path outputDirectory;
    private Map<String, String> defaultValues;
}
