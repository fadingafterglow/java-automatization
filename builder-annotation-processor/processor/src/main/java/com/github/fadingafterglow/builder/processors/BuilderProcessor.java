package com.github.fadingafterglow.builder.processors;

import com.github.fadingafterglow.builder.annotations.Builder;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

public class BuilderProcessor extends AbstractProcessor {

    private final BuilderCodeGenerator codeGenerator;

    public BuilderProcessor() {
        this.codeGenerator = new BuilderCodeGenerator();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Builder.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Builder.class)) {
            if (!validForGeneration(element)) continue;
            try {
                codeGenerator.generateBuilderCode((TypeElement) element).writeTo(processingEnv.getFiler());
            }
            catch (IOException e) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Failed to generate builder code: " + e.getMessage(),
                        element
                );
            }
        }
        return true;
    }

    private boolean validForGeneration(Element element) {
        if (!isNonAbstractClass(element)) {
            logError("be a non-abstract class", element);
            return false;
        }
        if (!isAccessible(element)) {
            logError("be public", element);
            return false;
        }
        if (!hasNoArgsAccessibleConstructor(element)) {
            logError("have a public no-args constructor", element);
            return false;
        }
        return true;
    }

    private boolean isNonAbstractClass(Element element) {
        return element.getKind().isClass() && !element.getModifiers().contains(Modifier.ABSTRACT);
    }

    private boolean isAccessible(Element element) {
        return element.getModifiers().contains(Modifier.PUBLIC);
    }

    private boolean hasNoArgsAccessibleConstructor(Element element) {
        return element.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.CONSTRUCTOR)
                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
                .anyMatch(e -> ((ExecutableElement) e).getParameters().isEmpty());
    }

    private void logError(String message, Element element) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                String.format("Element annotated with @%s must %s", Builder.class.getSimpleName(), message),
                element
        );
    }
}
