package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.spi.types.TypeHandlingException;
import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BasicProxyTest extends JfmProxyTestBase {
    private final BasicProxyResource basicProxyResource;

    BasicProxyTest() {
        super(root -> {
            Files.write(root.resolve("name"), "sample name".getBytes());
            Files.write(root.resolve("optional-name"), "sample name".getBytes());
            Files.write(root.resolve("optional-empty"), "".getBytes());
            Files.write(root.resolve("number"), "1234".getBytes());
            Files.write(root.resolve("integer"), "1234".getBytes());
            Files.write(root.resolve("optional-number"), "1234".getBytes());
            Files.write(root.resolve("optional-number-empty"), "".getBytes());
            Files.write(root.resolve("invalid-number"), "asdf".getBytes());
            Files.write(root.resolve("byte"), "a".getBytes());
            Files.write(root.resolve("char"), "\u0105".getBytes());
            Files.write(root.resolve("float"), "12.34".getBytes());
            Files.write(root.resolve("double"), "12.34".getBytes());
        });

        this.basicProxyResource = FilesystemMapper.instance()
                .getTarget(super.getRoot())
                .proxy(BasicProxyResource.class);
    }

    @Test
    void testName() {
        assertThat(basicProxyResource.getName())
                .isEqualTo("sample name");
    }

    @Test
    void testOptionalName() {
        assertThat(basicProxyResource.getOptionalName())
                .isPresent()
                .hasValue("sample name");
    }

    @Test
    void testOptionalEmpty() {
        assertThat(basicProxyResource.getOptionalEmpty())
                .isNotPresent();
    }

    @Test
    void testInt() {
        assertThat(basicProxyResource.getInt())
                .isEqualTo(1234);
    }

    @Test
    void testOptionalInt() {
        assertThat(basicProxyResource.getOptionalInt())
                .isPresent()
                .hasValue(1234);
    }

    @Test
    void testSetOptionalInt() {
        basicProxyResource.setOptionalInt(OptionalInt.empty());
        assertThat(basicProxyResource.getOptionalInt())
                .isNotPresent();

        basicProxyResource.setOptionalInt(OptionalInt.of(4321));
        assertThat(basicProxyResource.getOptionalInt())
                .isPresent()
                .hasValue(4321);
    }

    @Test
    void testRemoveOptionalInt() {
        basicProxyResource.emptyOptionalInt();
        assertThat(basicProxyResource.getOptionalInt())
                .isNotPresent();
    }

    @Test
    void testOptionalIntEmpty() {
        assertThat(basicProxyResource.getOptionalIntEmpty())
                .isNotPresent();
    }

    @Test
    void testInteger() {
        assertThat(basicProxyResource.getInteger())
                .isEqualTo(1234);
    }

    @Test
    void testInvalidNumber() {
        assertThatThrownBy(basicProxyResource::getInvalidInteger)
                .isInstanceOf(TypeHandlingException.class)
                .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void testByte() {
        assertThat(basicProxyResource.getByte())
                .isEqualTo((byte) 'a');
    }

    @Test
    void testSetByte() {
        basicProxyResource.setByte((byte) -7);
        assertThat(basicProxyResource.getByte())
                .isEqualTo((byte) -7);
    }

    @Test
    void testChar() {
        assertThat(basicProxyResource.getChar())
                .isEqualTo('\u0105');
    }

    @Test
    void testSetChar() {
        basicProxyResource.setChar('\u0106');
        assertThat(basicProxyResource.getChar())
                .isEqualTo('\u0106');
    }

    @Test
    void testFloat() {
        assertThat(basicProxyResource.getFloat())
                .isEqualTo(12.34f);
    }

    @Test
    void testSetFloat() {
        basicProxyResource.setFloat(43.21f);
        assertThat(basicProxyResource.getFloat())
                .isEqualTo(43.21f);
    }

    @Test
    void testDouble() {
        assertThat(basicProxyResource.getDouble())
                .isEqualTo(12.34d);
    }

    @Test
    void testSetDouble() {
        basicProxyResource.setDouble(43.21d);
        assertThat(basicProxyResource.getDouble())
                .isEqualTo(43.21d);
    }
}
