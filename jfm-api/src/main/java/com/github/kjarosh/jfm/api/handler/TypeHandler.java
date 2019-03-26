package com.github.kjarosh.jfm.api.handler;

import java.io.IOException;
import java.nio.file.Path;

public interface TypeHandler<T> {
    Class<T> getHandledClass();

    T handleRead(Path path) throws IOException;
}
