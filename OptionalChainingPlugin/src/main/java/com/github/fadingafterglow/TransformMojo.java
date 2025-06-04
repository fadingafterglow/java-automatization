package com.github.fadingafterglow;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.List;

@Mojo(name = "transform", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class TransformMojo extends BaseTransformMojo {

    @Override
    protected List<String> getSourceRoots() {
        return project.getCompileSourceRoots();
    }
}
