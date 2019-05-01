package com.github.kjarosh.jfm.impl.mounter;

import com.github.kjarosh.jfm.tests.util.FuseFSTestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFS;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Jarosz
 */
class ThreadDelegatingFSTest extends FuseFSTestBase {
    private static final Logger logger = LoggerFactory.getLogger(ThreadDelegatingFSTest.class);

    private static long timeoutMillis = 80;

    @Mock
    private FuseFS inner;

    private ThreadDelegatingFS threadDelegatingFS;

    @BeforeAll
    static void probeMinimumTimeout() {
        FuseFS testFs = mock(FuseFS.class);
        FuseFS probingFs = new ThreadDelegatingFS(
                Executors.newSingleThreadExecutor(), testFs,
                Duration.ofSeconds(10));

        when(testFs.unlink(any()))
                .thenAnswer(inv -> {
                    // simulate method execution
                    Thread.sleep(5);
                    return 0;
                });

        long from = System.nanoTime();
        probingFs.unlink(null);
        long time = System.nanoTime() - from;

        timeoutMillis = TimeUnit.NANOSECONDS.toMillis(time * 5);
        logger.info("Using timeout = " + timeoutMillis);
    }

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
                    Thread.sleep(timeoutMillis * 2);
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
