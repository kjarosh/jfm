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
    private final BasicResource basicResource;

    BasicProxyTest() {
        super(root -> {
            Files.write(root.resolve("name"), "sample name".getBytes());
            Files.write(root.resolve("optional-name"), "sample name".getBytes());
            Files.write(root.resolve("number"), "1234".getBytes());
            Files.write(root.resolve("integer"), "1234".getBytes());
            Files.write(root.resolve("optional-number"), "1234".getBytes());
            Files.write(root.resolve("invalid-number"), "asdf".getBytes());
            Files.write(root.resolve("byte"), "a".getBytes());
            Files.write(root.resolve("char"), "\u0105".getBytes());
            Files.write(root.resolve("float"), "12.34".getBytes());
            Files.write(root.resolve("double"), "12.34".getBytes());
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

    @Test
    void testOptionalName() {
        assertThat(basicResource.getOptionalName())
                .isPresent()
                .hasValue("sample name");
    }

    @Test
    void testOptionalEmpty() {
        assertThat(basicResource.getOptionalEmpty())
                .isNotPresent();
    }

    @Test
    void testInt() {
        assertThat(basicResource.getInt())
                .isEqualTo(1234);
    }

    @Test
    void testOptionalInt() {
        assertThat(basicResource.getOptionalInt())
                .isPresent()
                .hasValue(1234);
    }

    @Test
    void testSetOptionalInt() {
        basicResource.setOptionalInt(OptionalInt.empty());
        assertThat(basicResource.getOptionalInt())
                .isNotPresent();

        basicResource.setOptionalInt(OptionalInt.of(4321));
        assertThat(basicResource.getOptionalInt())
                .isPresent()
                .hasValue(4321);
    }

    @Test
    void testRemoveOptionalInt() {
        basicResource.removeOptionalInt();
        assertThat(basicResource.getOptionalInt())
                .isNotPresent();
    }

    @Test
    void testOptionalIntEmpty() {
        assertThat(basicResource.getOptionalIntEmpty())
                .isNotPresent();
    }

    @Test
    void testInteger() {
        assertThat(basicResource.getInteger())
                .isEqualTo(1234);
    }

    @Test
    void testInvalidNumber() {
        assertThatThrownBy(basicResource::getInvalidInteger)
                .isInstanceOf(TypeHandlingException.class)
                .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void testByte() {
        assertThat(basicResource.getByte())
                .isEqualTo((byte) 'a');
    }

    @Test
    void testSetByte() {
        basicResource.setByte((byte) -7);
        assertThat(basicResource.getByte())
                .isEqualTo((byte) -7);
    }

    @Test
    void testChar() {
        assertThat(basicResource.getChar())
                .isEqualTo('\u0105');
    }

    @Test
    void testSetChar() {
        basicResource.setChar('\u0106');
        assertThat(basicResource.getChar())
                .isEqualTo('\u0106');
    }

    @Test
    void testFloat() {
        assertThat(basicResource.getFloat())
                .isEqualTo(12.34f);
    }

    @Test
    void testSetFloat() {
        basicResource.setFloat(43.21f);
        assertThat(basicResource.getFloat())
                .isEqualTo(43.21f);
    }

    @Test
    void testDouble() {
        assertThat(basicResource.getDouble())
                .isEqualTo(12.34d);
    }

    @Test
    void testSetDouble() {
        basicResource.setDouble(43.21d);
        assertThat(basicResource.getDouble())
                .isEqualTo(43.21d);
    }
}
