package com.github.kjarosh.jfm.tests;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.nio.file.Files;

public class JfmProxyTestBase extends JfmTestBase {
    @AfterEach
    void tearDown() throws IOException {
        Files.walkFileTree(root, new DeletingFileVisitor(root));
    }

    protected <T> T proxy(Class<T> resourceClass) {
        return FilesystemMapper.instance()
                .getTarget(root)
                .proxy(resourceClass);
    }
}
