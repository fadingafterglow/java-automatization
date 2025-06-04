package com.github.fadingafterglow;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.List;

@Mojo(name = "restore-test", defaultPhase = LifecyclePhase.TEST_COMPILE)
public class RestoreTestMojo extends BaseRestoreMojo {

    @Override
    protected List<String> getSourceRoots() {
        return project.getTestCompileSourceRoots();
    }

}
