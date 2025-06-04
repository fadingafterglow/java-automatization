package com.github.fadingafterglow.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

    public static Path addExtension(Path path, String extension) {
        String pathString = path.toString();
        return Path.of(pathString + extension);
    }

    public static Path removeExtension(Path path, String extension) {
        String pathString = path.toString();
        int index = pathString.lastIndexOf(extension);
        if (index == -1)
            return path;
        return Path.of(pathString.substring(0, index));
    }

    public static List<Path> getAllFilesWithExtension(Path directory, String extension) {
        try (Stream<Path> pathStream = Files.walk(directory)) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(extension))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
