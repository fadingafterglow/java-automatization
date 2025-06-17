package com.github.fadingafterglow.builder.processors;

import com.github.fadingafterglow.builder.annotations.Builder;
import com.palantir.javapoet.*;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

public class BuilderCodeGenerator {

    private static final String BUILDER_SUFFIX = "Builder";
    private static final String BUILD_METHOD_NAME = "build";

    public JavaFile generateBuilderCode(TypeElement type) {
        ClassName builderClass = createBuilderClass(type);
        FieldSpec objectField = createObjectField(type);
        TypeSpec builderSpec = TypeSpec.classBuilder(builderClass)
                .addModifiers(Modifier.PUBLIC)
                .addField(createObjectField(type))
                .addMethods(createBuilderMethods(type, builderClass, objectField))
                .addMethod(createBuildMethod(type, objectField))
                .build();
        return JavaFile.builder(builderClass.packageName(), builderSpec).build();
    }

    private ClassName createBuilderClass(TypeElement type) {
        Builder annotation = type.getAnnotation(Builder.class);
        String name = annotation.name().isBlank() ? type.getQualifiedName().toString() + BUILDER_SUFFIX : annotation.name();
        int lastDotIndex = name.lastIndexOf('.');
        String packageName = lastDotIndex == -1 ? "" : name.substring(0, lastDotIndex);
        String className = lastDotIndex == -1 ? name : name.substring(lastDotIndex + 1);
        return ClassName.get(packageName, className);
    }

    private FieldSpec createObjectField(TypeElement type) {
        TypeName objectType = TypeName.get(type.asType());
        return FieldSpec.builder(objectType, "object")
                .addModifiers(Modifier.PRIVATE)
                .initializer("new $T()", objectType)
                .build();
    }

    private List<MethodSpec> createBuilderMethods(TypeElement type, ClassName builderClass, FieldSpec objectField) {
        return getSetters(type).stream()
                .map(s -> createBuilderMethod(s, builderClass, objectField))
                .toList();
    }

    private List<ExecutableElement> getSetters(TypeElement type) {
        return type.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .map(e -> (ExecutableElement) e)
                .filter(e -> e.getSimpleName().toString().startsWith("set"))
                .filter(e -> e.getParameters().size() == 1)
                .toList();
    }

    private MethodSpec createBuilderMethod(ExecutableElement setter, ClassName builderClass, FieldSpec objectField) {
        String setterName = setter.getSimpleName().toString();
        return MethodSpec.methodBuilder(createBuilderMethodName(setterName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(setter.getParameters().getFirst().asType()), "value")
                .returns(builderClass)
                .addStatement("this.$N.$L(value)", objectField, setterName)
                .addStatement("return this")
                .build();
    }

    private String createBuilderMethodName(String setterName) {
        if (setterName.length() == 3)
            return setterName;
        return Character.toLowerCase(setterName.charAt(3)) + setterName.substring(4);
    }

    private MethodSpec createBuildMethod(TypeElement type, FieldSpec objectField) {
        return MethodSpec.methodBuilder(BUILD_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(type.asType()))
                .addStatement("return this.$N", objectField)
                .build();
    }
}
