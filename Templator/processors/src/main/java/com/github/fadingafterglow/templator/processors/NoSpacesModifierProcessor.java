package com.github.fadingafterglow.templator.processors;

import com.github.fadingafterglow.templator.core.ModifierProcessor;
import com.github.fadingafterglow.templator.core.WildcardModifierProcessor;

@ModifierProcessor(modifier = "ns")
public class NoSpacesModifierProcessor implements WildcardModifierProcessor {
    @Override
    public String process(String value) {
        return value.replaceAll("\\s", "");
    }
}
