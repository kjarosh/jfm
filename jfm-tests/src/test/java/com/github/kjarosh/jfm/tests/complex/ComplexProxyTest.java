package com.github.kjarosh.jfm.tests.complex;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class ComplexProxyTest extends JfmProxyTestBase {
    private final ComplexProxyResource complexProxyResource;

    ComplexProxyTest() {
        this.complexProxyResource = FilesystemMapper.instance()
                .getTarget(root)
                .proxy(ComplexProxyResource.class);
    }

    @Test
    void testProperties() throws IOException {
        Properties props = prepareProperties();
        String expectedProps = propsToString(props);

        write(root.resolve("props"), expectedProps);

        assertThat(complexProxyResource.getProperties())
                .isEqualTo(props);
    }

    @Test
    void testSetProperties() throws IOException {
        Properties props = prepareProperties();
        String expectedProps = propsToString(props);

        complexProxyResource.setProperties(props);

        assertThat(read(root.resolve("props")))
                .isEqualTo(expectedProps);
    }

    private Properties prepareProperties() {
        Properties props = new Properties();
        props.setProperty("prop1", "value1");
        props.setProperty("prop2", "value2");
        return props;
    }

    private String propsToString(Properties props) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Writer w = new OutputStreamWriter(bos);
        props.store(w, null);
        return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    }
}
