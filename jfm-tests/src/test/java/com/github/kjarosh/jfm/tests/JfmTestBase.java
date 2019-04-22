package com.github.kjarosh.jfm.tests;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.fail;

public class JfmTestBase {
    private static final Path TEST_DIR = Paths.get("./jfm-tests");
    protected final Path root;

    public JfmTestBase(String testType) {
        try {
            Files.createDirectories(TEST_DIR);
            this.root = Files.createTempDirectory(TEST_DIR, "jfm-test-" + testType + "-");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUpMocks() {
        MockitoAnnotations.initMocks(this);
    }

    protected Path getRoot() {
        return root;
    }

    protected String read(Path file) {
        try {
            byte[] readBytes = Files.readAllBytes(file);
            return new String(readBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void write(Path file, String value) {
        try {
            if (file.normalize().startsWith(getRoot())) {
                fail("Wrong path: " + file);
            }

            Files.createDirectories(file.getParent());
            Files.write(file, value.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void remove(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
