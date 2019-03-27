package com.github.kjarosh.jfm.tests.pathparams;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmTestBase;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class PathParamsTest extends JfmTestBase {
    private final PathParamsResource pathParamsResource;

    PathParamsTest() {
        super(root -> {
            Files.write(root.resolve("first"), "1".getBytes());
            Files.write(root.resolve("second"), "2".getBytes());
            Files.write(root.resolve("third"), "3".getBytes());
        });

        this.pathParamsResource = FilesystemMapper.instance()
                .getTarget(super.getRoot())
                .proxy(PathParamsResource.class);
    }

    @Test
    void testInteger() {
        assertThat(pathParamsResource.getInteger("first"))
                .isEqualTo(1);
        assertThat(pathParamsResource.getInteger("second"))
                .isEqualTo(2);
        assertThat(pathParamsResource.getInteger("third"))
                .isEqualTo(3);
    }
}
