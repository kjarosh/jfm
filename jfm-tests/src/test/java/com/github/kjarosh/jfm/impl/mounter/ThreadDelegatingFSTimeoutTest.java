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
class ThreadDelegatingFSTimeoutTest extends FuseFSTestBase {
    private static final long timeoutMillis = 10;

    @Mock
    private FuseFS inner;

    private ThreadDelegatingFS threadDelegatingFS;

    @BeforeEach
    void setUpMocks() {
        MockitoAnnotations.initMocks(this);

        threadDelegatingFS = new ThreadDelegatingFS(
                Executors.newSingleThreadExecutor(), inner,
                Duration.ofMillis(timeoutMillis));
    }

    protected Stream<DynamicTest> testMockedMethod(Function<FuseFS, Integer> method) {
        return Stream.of(
                DynamicTest.dynamicTest(
                        "Test timeout",
                        () -> testTimeout(method)));
    }

    private void testTimeout(Function<FuseFS, Integer> method) {
        when(method.apply(inner))
                .thenAnswer(invocation -> {
                    Thread.sleep(timeoutMillis * 10);
                    return 0;
                });

        assertThat(method.apply(threadDelegatingFS))
                .isEqualTo(-ErrorCodes.EBADFD());
    }
}
