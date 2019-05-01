package com.github.kjarosh.jfm.impl.mounter;

import com.github.kjarosh.jfm.tests.util.FuseFSTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFS;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Jarosz
 */
class ThreadDelegatingFSTest extends FuseFSTestBase {
    private static final int TIMEOUT_MILLIS = 80;

    @Mock
    private FuseFS inner;

    private ThreadDelegatingFS threadDelegatingFS;

    @BeforeEach
    void setUpMocks() {
        MockitoAnnotations.initMocks(this);

        threadDelegatingFS = new ThreadDelegatingFS(
                Executors.newSingleThreadExecutor(), inner,
                Duration.ofMillis(TIMEOUT_MILLIS));
    }

    protected Stream<DynamicTest> testMockedMethod(Function<FuseFS, Integer> method) {
        return Stream.of(
                DynamicTest.dynamicTest(
                        "Test valid value returning",
                        () -> testValidReturn(method)),
                DynamicTest.dynamicTest(
                        "Test timeout",
                        () -> testTimeout(method)),
                DynamicTest.dynamicTest(
                        "Test exception thrown",
                        () -> testExceptionThrown(method)));
    }

    private void testValidReturn(Function<FuseFS, Integer> method) {
        when(method.apply(inner))
                .thenReturn(17);

        assertThat(method.apply(threadDelegatingFS))
                .isEqualTo(17);
    }

    private void testTimeout(Function<FuseFS, Integer> method) {
        when(method.apply(inner))
                .thenAnswer(invocation -> {
                    Thread.sleep(TIMEOUT_MILLIS * 2);
                    return 0;
                });

        assertThat(method.apply(threadDelegatingFS))
                .isEqualTo(-ErrorCodes.EBADFD());
    }

    private void testExceptionThrown(Function<FuseFS, Integer> method) {
        when(method.apply(inner))
                .thenThrow(new RuntimeException());

        assertThat(method.apply(threadDelegatingFS))
                .isEqualTo(-ErrorCodes.EBADFD());
    }
}
