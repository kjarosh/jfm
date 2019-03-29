package com.github.kjarosh.jfm.spi.types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;

public interface ListingTypeHandler<T> extends TypeHandlerBase<T> {
    default boolean isAppropriate(Type actualType) {
        return true;
    }

    T list(Type actualType, Path path) throws IOException;
}
