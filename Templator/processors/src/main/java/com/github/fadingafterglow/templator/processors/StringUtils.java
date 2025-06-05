package com.github.fadingafterglow.templator.processors;

public abstract class StringUtils {
    public static String toFirstCapital(String value) {
        if (value == null || value.isEmpty())
            return "";
        char first = Character.toUpperCase(value.charAt(0));
        return first + value.substring(1);
    }

    public static String toFirstLower(String value) {
        if (value == null || value.isEmpty())
            return "";
        char first = Character.toLowerCase(value.charAt(0));
        return first + value.substring(1);
    }
}
