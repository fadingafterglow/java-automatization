package com.github.fadingafterglow;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

public class ImportCheckPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        TaskProvider<ImportCheckTask> importCheck = project.getTasks().register("importCheck", ImportCheckTask.class, task -> {
            task.setGroup("Verification");
            task.setDescription("Checks for duplicate and out-of-order imports in source files.");

            SourceSetContainer sourceSets = project.getExtensions().getByType(SourceSetContainer.class);
            sourceSets.forEach(sourceSet ->
                    task.getSourceFiles().from(sourceSet.getAllJava().getFiles())
            );
        });

        project.getTasks().named("compileJava").configure(compile -> compile.dependsOn(importCheck));
    }
}
