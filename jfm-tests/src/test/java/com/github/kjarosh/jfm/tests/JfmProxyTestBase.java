package com.github.kjarosh.jfm.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JfmProxyTestBase {
    public static final Path TEST_DIR = Paths.get("./jfm-tests");

    private final Path testDirPath;
    private final ThrowingConsumer<Path> setup;

    public JfmProxyTestBase(ThrowingConsumer<Path> setup) {
        try {
            Files.createDirectories(TEST_DIR);
            this.testDirPath = Files.createTempDirectory(TEST_DIR, "jfm-proxy-test-");
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

    protected static void write(Path file, String content) throws IOException {
        Files.createDirectories(file.getParent());
        Files.write(file, content.getBytes());
    }
}
