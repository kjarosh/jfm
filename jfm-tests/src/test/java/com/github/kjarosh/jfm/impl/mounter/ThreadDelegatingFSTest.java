package com.github.kjarosh.jfm.impl.mounter;

import com.github.kjarosh.jfm.tests.util.FuseFSTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
class ThreadDelegatingFSTest extends FuseFSTestBase {
    @Mock
    private FuseFS inner;

    private ThreadDelegatingFS threadDelegatingFS;

    @BeforeEach
    void setUp() {
        threadDelegatingFS = new ThreadDelegatingFS(
                Executors.newSingleThreadExecutor(), inner,
                Duration.ofSeconds(2));
    }

    protected Stream<DynamicTest> testMockedMethod(Function<FuseFS, Integer> method) {
        return Stream.of(
                DynamicTest.dynamicTest(
                        "Test valid value returning",
                        () -> testValidReturn(method)),
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

    private void testExceptionThrown(Function<FuseFS, Integer> method) {
        when(method.apply(inner))
                .thenThrow(new RuntimeException());

        assertThat(method.apply(threadDelegatingFS))
                .isEqualTo(-ErrorCodes.EBADFD());
    }
}
