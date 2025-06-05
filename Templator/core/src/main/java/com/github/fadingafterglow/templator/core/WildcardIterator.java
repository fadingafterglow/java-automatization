package com.github.fadingafterglow.templator.core;

import java.util.Optional;

public interface WildcardIterator {
    Optional<Wildcard> next();
    void replaceCurrent(String replacement);
    String getTemplateAfterReplacement();
}
