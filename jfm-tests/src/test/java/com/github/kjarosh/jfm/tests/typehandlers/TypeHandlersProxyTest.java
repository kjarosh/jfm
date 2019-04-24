package com.github.kjarosh.jfm.tests.typehandlers;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class TypeHandlersProxyTest extends JfmProxyTestBase {
    private final TypeHandlersResource typeHandlersResource;

    TypeHandlersProxyTest() {
        FilesystemMapper.instance().getTypeHandlerService()
                .registerHandlersFromPackage(TypeHandlersProxyTest.class.getPackage());

        this.typeHandlersResource = proxy(TypeHandlersResource.class);
    }

    @BeforeEach
    void setUp() {
        write(root.resolve("text"), "asdf");
        write(root.resolve("list"), "a,b,c");
    }

    @Test
    void testText() {
        assertThat(typeHandlersResource.getText().getData())
                .isEqualTo("asdf");
    }

    @Test
    void testStringList() {
        assertThat(typeHandlersResource.getStringList())
                .isEqualTo(Arrays.asList("a", "b", "c"));
    }

    @Test
    void testTextList() {
        assertThat(typeHandlersResource.getTextList())
                .extracting(Text::getData)
                .isEqualTo(Arrays.asList("a", "b", "c"));
    }
}
