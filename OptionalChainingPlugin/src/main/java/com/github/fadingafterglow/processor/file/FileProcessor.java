package com.github.fadingafterglow.processor.file;

import com.github.fadingafterglow.processor.expression.IExpressionProcessor;
import com.github.fadingafterglow.utils.Counter;
import com.github.fadingafterglow.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileProcessor implements IFileProcessor {

    private static final Pattern PATTERN = Pattern.compile("\\w+(\\?\\.\\w+(\\(\\w*\\))?)+");

    private final IExpressionProcessor expressionProcessor;

    public FileProcessor(IExpressionProcessor expressionProcessor) {
        this.expressionProcessor = expressionProcessor;
    }

    @Override
    public void process(Path path, String extension) {
        Matcher matcher = PATTERN.matcher(read(path));
        Counter counter = new Counter();
        String processed = matcher.replaceAll(mr -> {
            counter.inc();
            return expressionProcessor.process(mr.group());
        });
        if (counter.get() != 0)
            write(path, extension, processed);
    }

    @Override
    public void restore(Path path, String extension) {
        try {
            Path originalPath = FileUtils.removeExtension(path, extension);
            Files.move(path, originalPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String read(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void write(Path path, String extension, String content) {
        try {
            Files.move(path, FileUtils.addExtension(path, extension), StandardCopyOption.REPLACE_EXISTING);
            Files.writeString(path, content, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
