package com.github.kjarosh.jfm.tests.constants;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConstantsProxyTest extends JfmProxyTestBase {
    private final ConstantsProxyResource constantsProxyResource;

    ConstantsProxyTest() {
        this.constantsProxyResource = FilesystemMapper.instance()
                .getTarget(root)
                .proxy(ConstantsProxyResource.class);
    }

    @Test
    void testSetConstantBytes() {
        constantsProxyResource.setConstantBytes();

        assertThat(read(root.resolve("constant-bytes")))
                .isEqualTo("asdf");

    }

    @Test
    void testSetConstantInt() {
        constantsProxyResource.setConstantInt();

        assertThat(read(root.resolve("constant-int")))
                .isEqualTo("7");
    }

    @Test
    void testSetConstantBoolean() {
        constantsProxyResource.setConstantBoolean();

        assertThat(read(root.resolve("constant-boolean")))
                .isEqualTo("true");
    }

    @Test
    void testSetConstantString() {
        constantsProxyResource.setConstantString();

        assertThat(read(root.resolve("constant-string")))
                .isEqualTo("asdf");
    }
}
