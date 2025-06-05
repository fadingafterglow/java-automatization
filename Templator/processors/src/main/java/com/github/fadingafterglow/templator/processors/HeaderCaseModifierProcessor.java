package com.github.fadingafterglow.templator.processors;

import com.github.fadingafterglow.templator.core.ModifierProcessor;
import com.github.fadingafterglow.templator.core.WildcardModifierProcessor;

@ModifierProcessor(modifier = "h")
public class HeaderCaseModifierProcessor implements WildcardModifierProcessor {
    @Override
    public String process(String value) {
        String[] words = value.split(" ");
        for (int i = 0; i < words.length; i++)
            words[i] = StringUtils.toFirstCapital(words[i]);
        return String.join(" ", words);
    }
}
