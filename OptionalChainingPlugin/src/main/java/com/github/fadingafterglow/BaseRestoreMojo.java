package com.github.fadingafterglow;

import com.github.fadingafterglow.utils.FileUtils;

import java.nio.file.Path;

public abstract class BaseRestoreMojo extends BaseMojo {

    @Override
    protected void processSourceRoot(Path sourceRoot) {
        for (Path source : FileUtils.getAllFilesWithExtension(sourceRoot, extension)) {
            getLog().info("Restoring: " + source);
            processor.restore(source, extension);
        }
    }

}
