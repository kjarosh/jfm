package com.github.kjarosh.jfm.impl.mounter;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperMounterFactory {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private FilesystemMapperMounterFactory() {

    }

    public static FilesystemMapperMounterFactory create() {
        return new FilesystemMapperMounterFactory();
    }

    public FilesystemMapperMounter newMounter(Path mountPoint) {
        return new FilesystemMapperMounter(executorService, mountPoint);
    }
}
