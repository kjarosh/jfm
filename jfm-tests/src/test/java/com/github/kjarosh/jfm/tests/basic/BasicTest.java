package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmTestBase;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicTest extends JfmTestBase {
    private final BasicResource basicResource;

    public BasicTest() {
        super(root -> {
            Files.write(root.resolve("name"), "sample name".getBytes());
        });

        this.basicResource = FilesystemMapper.instance()
                .getTarget(super.getRoot())
                .proxy(BasicResource.class);
    }

    @Test
    void testName() {
        assertThat(basicResource.getName())
                .isEqualTo("sample name");
    }
}
