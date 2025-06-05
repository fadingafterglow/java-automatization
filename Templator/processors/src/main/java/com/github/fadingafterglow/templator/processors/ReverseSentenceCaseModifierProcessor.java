package com.github.fadingafterglow.templator.processors;

import com.github.fadingafterglow.templator.core.ModifierProcessor;
import com.github.fadingafterglow.templator.core.WildcardModifierProcessor;

@ModifierProcessor(modifier = "rs")
public class ReverseSentenceCaseModifierProcessor implements WildcardModifierProcessor {
    @Override
    public String process(String value) {
        String[] words = value.split(" ");
        if (words.length > 0)
            words[0] = StringUtils.toFirstLower(words[0]);
        for (int i = 1; i < words.length; i++)
            words[i] = StringUtils.toFirstCapital(words[i]);
        return String.join(" ", words);
    }
}
