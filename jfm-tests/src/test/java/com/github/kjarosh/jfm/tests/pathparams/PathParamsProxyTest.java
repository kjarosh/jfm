package com.github.kjarosh.jfm.tests.pathparams;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathParamsProxyTest extends JfmProxyTestBase {
    private final PathParamsProxyResource pathParamsProxyResource;

    PathParamsProxyTest() {
        this.pathParamsProxyResource = proxy(PathParamsProxyResource.class);
    }

    @Test
    void testInteger() {
        write(root.resolve("first"), "1");
        write(root.resolve("second"), "2");
        write(root.resolve("third"), "3");

        assertThat(pathParamsProxyResource.getInteger("first"))
                .isEqualTo(1);
        assertThat(pathParamsProxyResource.getInteger("second"))
                .isEqualTo(2);
        assertThat(pathParamsProxyResource.getInteger("third"))
                .isEqualTo(3);
    }

    @Test
    void testInnerInteger() {
        write(root.resolve("first.d/int"), "1");
        write(root.resolve("second.d/int"), "2");
        write(root.resolve("third.d/int"), "3");

        assertThat(pathParamsProxyResource.getInnerInteger("first.d"))
                .isEqualTo(1);
        assertThat(pathParamsProxyResource.getInnerInteger("second.d"))
                .isEqualTo(2);
        assertThat(pathParamsProxyResource.getInnerInteger("third.d"))
                .isEqualTo(3);
    }

    @Test
    void testDenySeparators() {
        write(root.resolve("first.d/int"), "1");

        assertThatThrownBy(() -> pathParamsProxyResource.getInteger("first.d/int"))
                .isInstanceOf(FilesystemMapperException.class)
                .hasMessageContaining("Parameter value contains separators");
    }

    @Test
    void testAllowSeparators() {
        write(root.resolve("first.d/int"), "1");
        write(root.resolve("second.d/int"), "2");
        write(root.resolve("third.d/int"), "3");

        assertThat(pathParamsProxyResource.getAnyInteger("first.d/int"))
                .isEqualTo(1);
        assertThat(pathParamsProxyResource.getAnyInteger("second.d/int"))
                .isEqualTo(2);
        assertThat(pathParamsProxyResource.getAnyInteger("third.d/int"))
                .isEqualTo(3);
    }

    @Test
    void testRegex() {
        write(root.resolve("first"), "1");
        write(root.resolve("second.int"), "2");
        write(root.resolve("third"), "3");

        assertThat(pathParamsProxyResource.getIntegerWithRegex("first"))
                .isEqualTo(1);
        assertThatThrownBy(() -> pathParamsProxyResource.getIntegerWithRegex("second.int"))
                .isInstanceOf(FilesystemMapperException.class)
                .hasMessageContaining("Parameter value is not compliant with regular expression");
        assertThatThrownBy(() -> pathParamsProxyResource.getIntegerWithRegex("third/int"))
                .isInstanceOf(FilesystemMapperException.class)
                .hasMessageContaining("Parameter value contains separators");
    }
}
