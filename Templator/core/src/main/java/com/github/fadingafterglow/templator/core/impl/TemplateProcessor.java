package com.github.fadingafterglow.templator.core.impl;

import com.github.fadingafterglow.templator.core.Wildcard;
import com.github.fadingafterglow.templator.core.WildcardIterator;
import com.github.fadingafterglow.templator.core.WildcardResolver;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class TemplateProcessor {
    private final Function<String, WildcardIterator> wildcardIteratorProducer;
    private final Function<Map<String, String>, WildcardResolver> wildcardResolverProducer;

    public String process(String template, Map<String, String> wildcardValues) {
        WildcardIterator iterator = wildcardIteratorProducer.apply(template);
        WildcardResolver resolver = wildcardResolverProducer.apply(wildcardValues);
        while (true) {
            Optional<Wildcard> next = iterator.next();
            if (next.isEmpty())
                break;
            iterator.replaceCurrent(resolver.resolve(next.get()));
        }
        return iterator.getTemplateAfterReplacement();
    }
}
