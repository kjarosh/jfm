package com.github.kjarosh.jfm.handlers.complex;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Properties;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class PropertiesTypeHandler implements TypeHandler<Properties> {
    @Override
    public TypeReference<Properties> getHandledType() {
        return new TypeReference<Properties>() {
        };
    }

    @Override
    public Properties read(Type actualType, InputStream input) throws IOException {
        Properties ret = new Properties();
        ret.load(input);
        return ret;
    }

    @Override
    public void write(Type actualType, OutputStream output, Properties props) throws IOException {
        props.store(output, null);
    }

    @Override
    public byte[] serialize(Type actualType, Properties props) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            props.store(stream, null);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return stream.toByteArray();
    }

    @Override
    public Properties deserialize(Type actualType, byte[] data) {
        Properties ret = new Properties();
        try {
            ret.load(new ByteArrayInputStream(data));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return ret;
    }
}
