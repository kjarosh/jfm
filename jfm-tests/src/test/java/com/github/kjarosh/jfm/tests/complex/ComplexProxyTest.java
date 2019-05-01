package com.github.kjarosh.jfm.tests.complex;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmProxyTestBase;
import org.junit.jupiter.api.Test;

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
    void testProperties() {
        Properties props = prepareProperties();
        String expectedProps = PropertiesUtils.propsToString(props);

        write(root.resolve("props"), expectedProps);

        assertThat(complexProxyResource.getProperties())
                .isEqualTo(props);
    }

    @Test
    void testSetProperties() {
        Properties props = prepareProperties();
        String[] expectedProps = PropertiesUtils.propsToStrings(props);

        complexProxyResource.setProperties(props);

        assertThat(read(root.resolve("props")))
                .contains(expectedProps);
    }

    private Properties prepareProperties() {
        Properties props = new Properties();
        props.setProperty("prop1", "value1");
        props.setProperty("prop2", "value2");
        return props;
    }
}
