package com.github.fadingafterglow.templator.processors;

import com.github.fadingafterglow.templator.core.ModifierProcessor;
import com.github.fadingafterglow.templator.core.WildcardModifierProcessor;

@ModifierProcessor(modifier = "sd")
public class SpaceToDotModifierProcessor implements WildcardModifierProcessor {
    @Override
    public String process(String value) {
        return value.replaceAll("\\s", ".");
    }
}
