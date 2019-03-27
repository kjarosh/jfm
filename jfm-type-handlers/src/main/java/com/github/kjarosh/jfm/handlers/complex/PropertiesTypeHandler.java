package com.github.kjarosh.jfm.handlers.complex;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@RegisterTypeHandler
public class PropertiesTypeHandler implements TypeHandler<Properties> {
    @Override
    public TypeReference<Properties> getHandledType() {
        return new TypeReference<Properties>() {
        };
    }

    @Override
    public Properties handleRead(Type actualType, Path path) throws IOException {
        Properties ret = new Properties();
        ret.load(Files.newInputStream(path));
        return ret;
    }
}
