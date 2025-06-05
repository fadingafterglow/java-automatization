package com.github.fadingafterglow.templator.core.impl;

import com.github.fadingafterglow.templator.core.ModifierProcessor;
import com.github.fadingafterglow.templator.core.WildcardModifierProcessor;
import com.google.common.reflect.ClassPath;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WildcardModifierProcessorRegistry {
    private static final Map<String, WildcardModifierProcessor> registeredModifierProcessors = new HashMap<>();

    public static Map<String, WildcardModifierProcessor> getRegisteredModifierProcessors() {
        return new HashMap<>(registeredModifierProcessors);
    }

    public static void registerModifierProcessor(String modifier, WildcardModifierProcessor processor) {
        registeredModifierProcessors.put(modifier, processor);
    }

    public static void unregisterModifierProcessor(String modifier) {
        registeredModifierProcessors.remove(modifier);
    }

    public static void registerModifierProcessors(String packageName) {
        Set<Class<?>> processorClasses = findModifierProcessorClasses(packageName);
        for (Class<?> processorClass : processorClasses) {
            try {
                ModifierProcessor annotation = processorClass.getAnnotation(ModifierProcessor.class);
                if (annotation == null)
                    continue;
                WildcardModifierProcessor processor = (WildcardModifierProcessor) processorClass.getConstructor().newInstance();
                registeredModifierProcessors.put(annotation.modifier(), processor);
            }
            catch (Exception e) {
                // ignore
            }
        }
    }

    @SneakyThrows
    private static Set<Class<?>> findModifierProcessorClasses(String packageName) {
        return ClassPath.from(Thread.currentThread().getContextClassLoader())
                .getAllClasses().stream()
                .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
                .map(ClassPath.ClassInfo::load)
                .filter(WildcardModifierProcessor.class::isAssignableFrom)
                .collect(Collectors.toSet());
    }
}
