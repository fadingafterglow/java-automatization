package com.github.fadingafterglow;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.List;

@Mojo(name = "transform-test", defaultPhase = LifecyclePhase.PROCESS_TEST_SOURCES)
public class TransformTestMojo extends BaseTransformMojo {

    @Override
    protected List<String> getSourceRoots() {
        return project.getTestCompileSourceRoots();
    }
}
