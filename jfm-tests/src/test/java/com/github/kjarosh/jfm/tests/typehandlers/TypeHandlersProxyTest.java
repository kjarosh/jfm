package com.github.kjarosh.jfm.tests.typehandlers;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeHandlersProxyTest extends JfmProxyTestBase {
    private final TypeHandlersResource typeHandlersResource;

    public TypeHandlersProxyTest() {
        super(root -> {
            Files.write(root.resolve("text"), "asdf".getBytes());
            Files.write(root.resolve("list"), "a,b,c".getBytes());
        });

        FilesystemMapper.instance().getTypeHandlerService()
                .registerHandlersFromPackage(TypeHandlersProxyTest.class.getPackage());

        this.typeHandlersResource = FilesystemMapper.instance()
                .getTarget(super.getRoot())
                .proxy(TypeHandlersResource.class);
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
