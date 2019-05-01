package com.github.kjarosh.jfm.impl.mounter;

import com.github.kjarosh.jfm.tests.util.FuseFSTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.serce.jnrfuse.FuseFS;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Jarosz
 */
class ErrorCodeExceptionHandlingFSTest extends FuseFSTestBase {
    @Mock
    private FuseFS inner;

    private ErrorCodeExceptionHandlingFS exceptionHandlingFS;

    @BeforeEach
    void setUpMocks() {
        MockitoAnnotations.initMocks(this);

        exceptionHandlingFS = new ErrorCodeExceptionHandlingFS(inner);
    }

    protected Stream<DynamicTest> testMockedMethod(Function<FuseFS, Integer> method) {
        return Stream.of(
                DynamicTest.dynamicTest(
                        "Test ErrorCodeException throwing",
                        () -> testExceptionThrow(method)));
    }

    private void testExceptionThrow(Function<FuseFS, Integer> method) {
        when(method.apply(inner))
                .thenThrow(new ErrorCodeException(15));

        assertThat(method.apply(exceptionHandlingFS))
                .isEqualTo(15);
    }
}