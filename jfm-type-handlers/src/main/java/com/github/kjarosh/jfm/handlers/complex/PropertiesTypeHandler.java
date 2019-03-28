package com.github.kjarosh.jfm.handlers.complex;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import com.github.kjarosh.jfm.api.types.TypeReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    public Properties read(Type actualType, Path path) throws IOException {
        Properties ret = new Properties();
        ret.load(Files.newInputStream(path));
        return ret;
    }

    @Override
    public void write(Type actualType, Path path, Properties props) throws IOException {
        props.store(Files.newOutputStream(path), null);
    }

    @Override
    public byte[] serialize(Type actualType, Properties props) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            props.store(stream, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stream.toByteArray();
    }

    @Override
    public Properties deserialize(Type actualType, byte[] data) {
        Properties ret = new Properties();
        try {
            ret.load(new ByteArrayInputStream(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
