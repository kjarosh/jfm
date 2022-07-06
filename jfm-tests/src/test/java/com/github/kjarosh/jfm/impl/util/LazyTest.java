package com.github.kjarosh.jfm.impl.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Kamil Jarosz
 */
class LazyTest {
    @Test
    void eagerValue() {
        Lazy<String, RuntimeException> lazy = Lazy.ofEager("a");
        assertThat(lazy.get()).isEqualTo("a");
    }

    @SuppressWarnings("unchecked")
    @Test
    void lazyValue() {
        ThrowingSupplier<String, RuntimeException> supplier = Mockito.mock(ThrowingSupplier.class);
        when(supplier.get()).thenReturn("a");
        Lazy<String, RuntimeException> lazy = Lazy.of(supplier);

        verify(supplier, times(0)).get();
        assertThat(lazy.get()).isEqualTo("a");
        verify(supplier, times(1)).get();
        assertThat(lazy.get()).isEqualTo("a");
        verify(supplier, times(1)).get();
    }

    @SuppressWarnings("unchecked")
    @Test
    void simultaneous() {
        ExecutorService es = Executors.newFixedThreadPool(2);
        Phaser phaser = new Phaser(3);

        ThrowingSupplier<String, RuntimeException> supplier = Mockito.mock(ThrowingSupplier.class);
        when(supplier.get()).thenReturn("a");
        Lazy<String, RuntimeException> lazy = Lazy.of(supplier);

        es.submit(() -> {
            phaser.arriveAndAwaitAdvance();
            assertThat(lazy.get()).isEqualTo("a");
            phaser.arriveAndAwaitAdvance();
        });
        es.submit(() -> {
            phaser.arriveAndAwaitAdvance();
            assertThat(lazy.get()).isEqualTo("a");
            phaser.arriveAndAwaitAdvance();
        });

        verify(supplier, times(0)).get();
        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndAwaitAdvance();
        verify(supplier, times(1)).get();
    }
}
