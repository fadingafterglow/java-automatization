package com.github.fadingafterglow;

import com.github.fadingafterglow.utils.FileUtils;

import java.nio.file.Path;

public abstract class BaseTransformMojo extends BaseMojo {

    @Override
    protected void processSourceRoot(Path sourceRoot) {
        for (Path source : FileUtils.getAllFilesWithExtension(sourceRoot, ".java")) {
            getLog().info("Processing: " + source);
            processor.process(source, extension);
        }
    }
}
