package com.github.kjarosh.jfm.impl.util;

/**
 * @author Kamil Jarosz
 */
public class Lazy<R, T extends Throwable> {
    private final ThrowingSupplier<R, T> supplier;

    private volatile boolean valueComputed;
    private volatile R value;

    private Lazy(ThrowingSupplier<R, T> supplier) {
        this.supplier = supplier;
    }

    private Lazy(R value) {
        this.supplier = () -> value;
        this.valueComputed = true;
        this.value = value;
    }

    public static <R, T extends Throwable> Lazy<R, T> of(ThrowingSupplier<R, T> supplier) {
        return new Lazy<>(supplier);
    }

    public static <R, T extends Throwable> Lazy<R, T> ofEager(R value) {
        return new Lazy<>(value);
    }

    public R get() throws T {
        if (valueComputed) {
            return value;
        } else {
            synchronized (this) {
                if (valueComputed) {
                    return value;
                }

                value = compute();
                valueComputed = true;
                return value;
            }
        }
    }

    private R compute() throws T {
        return supplier.get();
    }
}
