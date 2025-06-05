package com.github.fadingafterglow.templator.processors;

import com.github.fadingafterglow.templator.core.ModifierProcessor;
import com.github.fadingafterglow.templator.core.WildcardModifierProcessor;

import java.util.Locale;

@ModifierProcessor(modifier = "u")
public class UppercaseModifierProcessor implements WildcardModifierProcessor {
    @Override
    public String process(String value) {
        return value.toUpperCase(Locale.ROOT);
    }
}
