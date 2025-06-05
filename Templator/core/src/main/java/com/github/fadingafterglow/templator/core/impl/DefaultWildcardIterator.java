package com.github.fadingafterglow.templator.core.impl;

import com.github.fadingafterglow.templator.core.WildcardIterator;
import com.github.fadingafterglow.templator.core.Wildcard;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultWildcardIterator implements WildcardIterator {
    public static final String LEFT_DELIMITER = "[:";
    public static final String RIGHT_DELIMITER = ":]";
    private static final Pattern WILDCARD_PATTERN = Pattern.compile(
            Pattern.quote(LEFT_DELIMITER) +
                  "(?<name>\\w+)(?<modifiers>(-[a-zA-Z]+)*)" +
                  Pattern.quote(RIGHT_DELIMITER)
    );

    private final Matcher matcher;
    private final StringBuffer resultBuffer;

    public DefaultWildcardIterator(String template) {
        matcher = WILDCARD_PATTERN.matcher(template);
        resultBuffer = new StringBuffer();
    }

    public Optional<Wildcard> next() {
        if (!matcher.find())
            return Optional.empty();
        String name = matcher.group("name");
        Set<String> modifiers = splitModifiers(matcher.group("modifiers"));
        return Optional.of(new Wildcard(name, modifiers));
    }

    private Set<String> splitModifiers(String modifiers) {
        Set<String> result = new LinkedHashSet<>();
        String[] afterSplit = modifiers.split("-");
        for (String str : afterSplit)
            if (!str.isBlank())
                result.add(str);
        return result;
    }

    public void replaceCurrent(String replacement) {
        matcher.appendReplacement(resultBuffer, replacement);
    }

    public String getTemplateAfterReplacement() {
        return matcher.appendTail(resultBuffer).toString();
    }
}
