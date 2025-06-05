package com.github.fadingafterglow.templator.processors;

import com.github.fadingafterglow.templator.core.ModifierProcessor;
import com.github.fadingafterglow.templator.core.WildcardModifierProcessor;

@ModifierProcessor(modifier = "s")
public class SentenceCaseModifierProcessor implements WildcardModifierProcessor {
    @Override
    public String process(String value) {
        String[] words = value.split(" ", 2);
        words[0] = StringUtils.toFirstCapital(words[0]);
        return String.join(" ", words);
    }
}
