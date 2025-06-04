package com.github.fadingafterglow;

import com.github.fadingafterglow.processor.expression.ExpressionProcessor;
import com.github.fadingafterglow.processor.file.FileProcessor;
import com.github.fadingafterglow.processor.file.IFileProcessor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.List;

public abstract class BaseMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = true, required = true)
    protected MavenProject project;

    @Parameter(property = "optional.chaining.extension", defaultValue = ".original")
    protected String extension;

    protected final IFileProcessor processor;

    public BaseMojo() {
        this.processor = new FileProcessor(new ExpressionProcessor());
    }

    @Override
    public void execute() throws MojoExecutionException {
        try {
            getSourceRoots().forEach(sr -> processSourceRoot(Path.of(sr)));
        }
        catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    protected abstract List<String> getSourceRoots();

    protected abstract void processSourceRoot(Path sourceRoot);
}
