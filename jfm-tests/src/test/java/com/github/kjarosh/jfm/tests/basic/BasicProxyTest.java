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
        this.basicProxyResource = FilesystemMapper.instance()
                .getTarget(root)
                .proxy(BasicProxyResource.class);
    }

    @Test
    void testName() {
        write(root.resolve("string"), "sample string");

        assertThat(basicProxyResource.getString())
                .isEqualTo("sample string");
    }

    @Test
    void testOptionalName() {
        write(root.resolve("optional-string"), "sample string");

        assertThat(basicProxyResource.getOptionalString())
                .isPresent()
                .hasValue("sample string");
    }

    @Test
    void testOptionalEmpty() {
        write(root.resolve("optional-string-empty"), "");

        assertThat(basicProxyResource.getOptionalStringEmpty())
                .isNotPresent();
    }

    @Test
    void testInt() {
        write(root.resolve("int"), "1234");

        assertThat(basicProxyResource.getInt())
                .isEqualTo(1234);
    }

    @Test
    void testOptionalInt() {
        write(root.resolve("optional-int"), "1234");

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
    void testOptionalIntEmpty() {
        write(root.resolve("optional-int-empty"), "");

        assertThat(basicProxyResource.getOptionalIntEmpty())
                .isNotPresent();
    }

    @Test
    void testInteger() {
        write(root.resolve("integer"), "1234");

        assertThat(basicProxyResource.getInteger())
                .isEqualTo(1234);
    }

    @Test
    void testInvalidNumber() {
        write(root.resolve("invalid-int"), "asdf");

        assertThatThrownBy(basicProxyResource::getInvalidInt)
                .isInstanceOf(TypeHandlingException.class)
                .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void testByte() {
        write(root.resolve("byte"), "a");

        assertThat(basicProxyResource.getByte())
                .isEqualTo((byte) 'a');
    }

    @Test
    void testSetByte() {
        basicProxyResource.setByte((byte) -7);

        assertThat(read(root.resolve("byte")))
                .isEqualTo(new String(new byte[]{-7}));
    }

    @Test
    void testChar() {
        write(root.resolve("char"), "\u0105");

        assertThat(basicProxyResource.getChar())
                .isEqualTo('\u0105');
    }

    @Test
    void testSetChar() {
        basicProxyResource.setChar('\u0106');

        assertThat(read(root.resolve("char")))
                .isEqualTo("\u0106");
    }

    @Test
    void testFloat() {
        write(root.resolve("float"), "12.34");

        assertThat(basicProxyResource.getFloat())
                .isEqualTo(12.34f);
    }

    @Test
    void testSetFloat() {
        basicProxyResource.setFloat(43.21f);

        assertThat(read(root.resolve("float")))
                .isEqualTo("43.21");
    }

    @Test
    void testDouble() {
        write(root.resolve("double"), "12.34");

        assertThat(basicProxyResource.getDouble())
                .isEqualTo(12.34d);
    }

    @Test
    void testSetDouble() {
        basicProxyResource.setDouble(43.21d);

        assertThat(read(root.resolve("double")))
                .isEqualTo("43.21");
    }

    @Test
    void testRemoveFile() {
        write(root.resolve("removable-file"), "text");

        basicProxyResource.removeFile();

        assertThat(Files.exists(root.resolve("removable-file")))
                .isFalse();
    }

    @Test
    void testSetConstantBytes() {
        basicProxyResource.setConstantBytes();

        assertThat(read(root.resolve("constant-bytes")))
                .isEqualTo("asdf");

    }

    @Test
    void testSetConstantInt() {
        basicProxyResource.setConstantInt();

        assertThat(read(root.resolve("constant-int")))
                .isEqualTo("7");
    }

    @Test
    void testSetConstantBoolean() {
        basicProxyResource.setConstantBoolean();

        assertThat(read(root.resolve("constant-boolean")))
                .isEqualTo("true");
    }

    @Test
    void testSetConstantString() {
        basicProxyResource.setConstantString();

        assertThat(read(root.resolve("constant-string")))
                .isEqualTo("asdf");
    }
}
