package com.github.kjarosh.jfm.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JfmTestBase {
    public static final Path TEST_DIR = Paths.get("./jfm-tests");

    private final Path testDirPath;
    private final ThrowingConsumer<Path> setup;

    public JfmTestBase(ThrowingConsumer<Path> setup) {
        try {
            Files.createDirectories(TEST_DIR);
            this.testDirPath = Files.createTempDirectory(TEST_DIR, "jfm-test-");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setup = setup;
    }

    @BeforeEach
    void setUp() throws Throwable {
        setup.accept(testDirPath);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walkFileTree(testDirPath, new DeletingFileVisitor());
    }

    protected Path getRoot() {
        return testDirPath;
    }
}
