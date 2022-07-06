package com.github.kjarosh.jfm.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class JfmTestBase {
    private static final Path TEST_DIR = Paths.get("./jfm-tests");

    protected static Path root;

    @BeforeAll
    static void setUpTestDirectory() throws IOException {
        Files.createDirectories(TEST_DIR);
        root = Files.createTempDirectory(TEST_DIR, ".jfm-test-");
    }

    @AfterAll
    static void tearDownTestDirectory() {
        try {
            Files.walkFileTree(root, new DeletingFileVisitor(root));
            Files.delete(root);
            root = null;
        } catch (IOException ignored) {

        }
    }

    protected String read(Path file) {
        checkTestFilePath(file);

        try {
            byte[] readBytes = Files.readAllBytes(file);
            return new String(readBytes);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    protected void write(Path file, String value) {
        checkTestFilePath(file);

        try {
            Files.createDirectories(file.getParent());
            Files.write(file, value.getBytes());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    protected void remove(Path file) {
        checkTestFilePath(file);

        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    protected List<String> list(Path file) {
        checkTestFilePath(file);

        try (Stream<Path> list = Files.list(file)) {
            return list
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private void checkTestFilePath(Path file) {
        if (file.normalize().startsWith(root)) {
            fail("Wrong path: " + file);
        }
    }
}
