package com.github.kjarosh.jfm.api.types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;

public interface TypeHandler<T> {
    TypeReference<T> getHandledType();

    T handleRead(Type actualType, Path path) throws IOException;
}
