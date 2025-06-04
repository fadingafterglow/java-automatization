package com.github.fadingafterglow;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.List;

@Mojo(name = "restore", defaultPhase = LifecyclePhase.COMPILE)
public class RestoreMojo extends BaseRestoreMojo {

    @Override
    protected List<String> getSourceRoots() {
        return project.getCompileSourceRoots();
    }

}
