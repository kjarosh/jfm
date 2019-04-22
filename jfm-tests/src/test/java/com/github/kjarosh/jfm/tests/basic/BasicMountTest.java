package com.github.kjarosh.jfm.tests.basic;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.tests.JfmMountTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;
import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Jarosz
 */
class BasicMountTest extends JfmMountTestBase {
    private FilesystemMapper fm = FilesystemMapper.instance();

    @Mock
    private BasicMountResource basicMountResource;

    @BeforeEach
    void setUp() {
        fm.getTarget(root).mount(basicMountResource);
    }

    @AfterEach
    void tearDown() {
        fm.getTarget(root).umountAll();
    }

    @Test
    void testName() {
        when(basicMountResource.getName()).thenReturn("test name");

        assertThat(read(root.resolve("name")))
                .isEqualTo("test name");
    }

    @Test
    void testOptionalName() {
        when(basicMountResource.getOptionalName()).thenReturn(Optional.of("sample name"));

        assertThat(read(root.resolve("optional-name")))
                .isEqualTo("sample name");
    }

    @Test
    void testOptionalEmpty() {
        when(basicMountResource.getOptionalName()).thenReturn(Optional.empty());

        assertThat(read(root.resolve("optional-name")))
                .isEmpty();
    }

    @Test
    void testInt() {
        when(basicMountResource.getInt()).thenReturn(4321);

        assertThat(read(root.resolve("number")))
                .isEqualTo("4321");
    }

    @Test
    void testOptionalInt() {
        when(basicMountResource.getOptionalInt()).thenReturn(OptionalInt.of(4321));

        assertThat(read(root.resolve("optional-number")))
                .isEqualTo("4321");
    }

    @Test
    void testSetOptionalInt() {
        write(root.resolve("optional-number"), "4321");

        OptionalInt wantedArgument = OptionalInt.of(4321);
        verify(basicMountResource, times(1))
                .setOptionalInt(wantedArgument);
        verify(basicMountResource, never())
                .setOptionalInt(not(eq(wantedArgument)));
    }

    @Test
    void testRemoveOptionalInt() {
        remove(root.resolve("removable-string"));

        verify(basicMountResource, times(1))
                .removeString();
    }

    /*@Test
    void testInteger() {
        when(basicMountResource.getInteger()).thenReturn(4321);

        write(root.resolve("invalid-integer"), "not integer");
    }

    /*@Test
    void testInvalidNumber() {
        assertThatThrownBy(basicMountResource::getInvalidInt)
                .isInstanceOf(TypeHandlingException.class)
                .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    void testByte() {
        assertThat(basicMountResource.getByte())
                .isEqualTo((byte) 'a');
    }

    @Test
    void testSetByte() {
        basicMountResource.setByte((byte) -7);
        assertThat(basicMountResource.getByte())
                .isEqualTo((byte) -7);
    }

    @Test
    void testChar() {
        assertThat(basicMountResource.getChar())
                .isEqualTo('\u0105');
    }

    @Test
    void testSetChar() {
        basicMountResource.setChar('\u0106');
        assertThat(basicMountResource.getChar())
                .isEqualTo('\u0106');
    }

    @Test
    void testFloat() {
        assertThat(basicMountResource.getFloat())
                .isEqualTo(12.34f);
    }

    @Test
    void testSetFloat() {
        basicMountResource.setFloat(43.21f);
        assertThat(basicMountResource.getFloat())
                .isEqualTo(43.21f);
    }

    @Test
    void testDouble() {
        assertThat(basicMountResource.getDouble())
                .isEqualTo(12.34d);
    }

    @Test
    void testSetDouble() {
        basicMountResource.setDouble(43.21d);
        assertThat(basicMountResource.getDouble())
                .isEqualTo(43.21d);
    }*/
}
