package com.github.kjarosh.jfm.api;

import com.github.kjarosh.jfm.impl.FilesystemMapperImpl;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Jarosz
 */
@ExtendWith(MockitoExtension.class)
class FilesystemMapperProviderTest {
    @Mock
    private Reflections reflections;

    private Reflections oldReflections;

    @BeforeEach
    void setUp() {
        FilesystemMapperProvider.instance = null;
        oldReflections = FilesystemMapperProvider.jfmReflections;
        FilesystemMapperProvider.jfmReflections = reflections;
    }

    @AfterEach
    void restore() {
        FilesystemMapperProvider.jfmReflections = oldReflections;
    }

    @Test
    void noImplementations() {
        when(reflections.getSubTypesOf(FilesystemMapper.class))
                .thenReturn(new HashSet<>());

        assertThatThrownBy(FilesystemMapper::instance)
                .isInstanceOf(ImplementationLookupException.class);
    }

    @Test
    void oneImplementation() {
        when(reflections.getSubTypesOf(FilesystemMapper.class))
                .thenReturn(new HashSet<>(Collections.singletonList(FilesystemMapperImpl.class)));

        assertThat(FilesystemMapper.instance()).isNotNull();
    }

    @Test
    void multipleImplementations() {
        when(reflections.getSubTypesOf(FilesystemMapper.class))
                .thenReturn(new HashSet<>(Arrays.asList(
                        FilesystemMapperImpl.class,
                        NonInstantiableFilesystemMapper.class)));

        assertThatThrownBy(FilesystemMapper::instance)
                .isInstanceOf(ImplementationLookupException.class);
    }

    @Test
    void instantiationException() {
        when(reflections.getSubTypesOf(FilesystemMapper.class))
                .thenReturn(new HashSet<>(Collections.singletonList(
                        NonInstantiableFilesystemMapper.class)));

        assertThatThrownBy(FilesystemMapper::instance)
                .isInstanceOf(ImplementationLookupException.class);
    }

    @Test
    void loadSimultaneously() {
        ExecutorService es = Executors.newFixedThreadPool(2);
        Phaser phaser = new Phaser(3);

        when(reflections.getSubTypesOf(FilesystemMapper.class))
                .thenReturn(new HashSet<>(Collections.singletonList(
                        InstanceCountingFilesystemMapper.class)));

        es.submit(() -> {
            phaser.arriveAndAwaitAdvance();
            assertThat(FilesystemMapper.instance())
                    .isNotNull();
            phaser.arriveAndAwaitAdvance();
        });
        es.submit(() -> {
            phaser.arriveAndAwaitAdvance();
            assertThat(FilesystemMapper.instance())
                    .isNotNull();
            phaser.arriveAndAwaitAdvance();
        });

        InstanceCountingFilesystemMapper.instances.set(0);
        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndAwaitAdvance();
        assertThat(InstanceCountingFilesystemMapper.instances.get())
                .isEqualTo(1);
    }

    public static class InstanceCountingFilesystemMapper implements FilesystemMapper {
        static final AtomicInteger instances = new AtomicInteger(0);

        public InstanceCountingFilesystemMapper() {
            instances.incrementAndGet();
        }

        @Override
        public FilesystemMapperTarget getTarget(Path path) {
            return null;
        }

        @Override
        public TypeHandlerService getTypeHandlerService() {
            return null;
        }
    }

    public static class NonInstantiableFilesystemMapper implements FilesystemMapper {
        public NonInstantiableFilesystemMapper() {
            throw new RuntimeException();
        }

        @Override
        public FilesystemMapperTarget getTarget(Path path) {
            return null;
        }

        @Override
        public TypeHandlerService getTypeHandlerService() {
            return null;
        }
    }
}
