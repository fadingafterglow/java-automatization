package com.github.fadingafterglow.templator.client;

import java.nio.file.Path;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Configuration {
    private Path outputDirectory;
    private Map<String, String> defaultValues;
}
