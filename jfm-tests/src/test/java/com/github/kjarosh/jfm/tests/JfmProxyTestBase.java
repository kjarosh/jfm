package com.github.kjarosh.jfm.tests;

import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.nio.file.Files;

public class JfmProxyTestBase extends JfmTestBase {
    public JfmProxyTestBase() {
        super("proxy");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walkFileTree(getRoot(), new DeletingFileVisitor());
    }
}
