package com.github.kjarosh.jfm.tests.complex;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmMountTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Jarosz
 */
class ComplexMountTest extends JfmMountTestBase {
    private FilesystemMapper fm = FilesystemMapper.instance();

    @Mock
    private ComplexMountResource complexMountResource;

    @BeforeEach
    void setUp() {
        fm.getTarget(root).mount(complexMountResource);
    }

    @AfterEach
    void tearDown() {
        fm.getTarget(root).umountAll();
    }

    @Test
    void testProperties() throws IOException {
        Properties props = prepareProperties();
        String expectedProps = propsToString(props);

        when(complexMountResource.getProperties()).thenReturn(props);

        assertThat(read(root.resolve("props")))
                .isEqualTo(expectedProps);
    }

    @Test
    void testSetProperties() throws IOException {
        Properties props = prepareProperties();
        String expectedProps = propsToString(props);

        write(root.resolve("props"), expectedProps);

        verify(complexMountResource, times(1))
                .setProperties(props);
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
