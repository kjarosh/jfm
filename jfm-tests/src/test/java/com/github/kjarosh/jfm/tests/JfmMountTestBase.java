package com.github.kjarosh.jfm.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Kamil Jarosz
 */
public class JfmMountTestBase {
    public static final Path TEST_DIR = Paths.get("./jfm-tests");

    private final Path testDirPath;

    public JfmMountTestBase() {
        try {
            Files.createDirectories(TEST_DIR);
            this.testDirPath = Files.createTempDirectory(TEST_DIR, "jfm-mount-test-");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Path getRoot() {
        return testDirPath;
    }

    protected static String read(Path file) {
        try {
            return new String(Files.readAllBytes(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
