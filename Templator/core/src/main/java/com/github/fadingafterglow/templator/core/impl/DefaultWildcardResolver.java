package com.github.fadingafterglow.templator.core.impl;

import com.github.fadingafterglow.templator.core.Wildcard;
import com.github.fadingafterglow.templator.core.WildcardModifierProcessor;
import com.github.fadingafterglow.templator.core.WildcardResolver;

import java.util.HashMap;
import java.util.Map;

public class DefaultWildcardResolver implements WildcardResolver {

    private final Map<String, String> wildcardValues;
    private final Map<String, WildcardModifierProcessor> wildcardModifierProcessors;
    private final Map<Wildcard, String> wildcardCache;

    public DefaultWildcardResolver(Map<String, String> wildcardValues) {
        this.wildcardValues = wildcardValues;
        this.wildcardModifierProcessors = WildcardModifierProcessorRegistry.getRegisteredModifierProcessors();
        wildcardCache = new HashMap<>();
    }

    @Override
    public String resolve(Wildcard wildcard) {
        if (wildcardCache.containsKey(wildcard))
            return wildcardCache.get(wildcard);
        else {
            String result = computeValue(wildcard);
            wildcardCache.put(wildcard, result);
            return result;
        }
    }

    private String computeValue(Wildcard wildcard) {
        if (!wildcardValues.containsKey(wildcard.getName()))
            throw new RuntimeException("Value for wildcard \"" + wildcard.getName() + "\" is not specified");
        String value = wildcardValues.get(wildcard.getName());
        for (String modifier : wildcard.getModifiers()) {
            WildcardModifierProcessor processor = wildcardModifierProcessors.get(modifier);
            if (processor == null)
                throw new RuntimeException("Processor for modifier -" + modifier + " is not registered");
            value = processor.process(value);
        }
        return value;
    }
}
