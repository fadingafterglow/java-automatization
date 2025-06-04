package com.github.fadingafterglow.processor.file;

import java.nio.file.Path;

public interface IFileProcessor {

    void process(Path path, String extension);

    void restore(Path path, String extension);
}
