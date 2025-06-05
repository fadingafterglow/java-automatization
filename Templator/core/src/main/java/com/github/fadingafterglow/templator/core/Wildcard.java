package com.github.fadingafterglow.templator.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class Wildcard {
    private String name;
    private Set<String> modifiers;
}
