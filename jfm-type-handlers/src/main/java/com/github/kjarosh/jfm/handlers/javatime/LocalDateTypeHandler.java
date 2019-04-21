package com.github.kjarosh.jfm.handlers.javatime;

import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class LocalDateTypeHandler implements TypeHandler<LocalDate> {
    @Override
    public TypeReference<LocalDate> getHandledType() {
        return new TypeReference<LocalDate>() {
        };
    }

    @Override
    public LocalDate deserialize(Type actualType, byte[] data) {
        return LocalDate.parse(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize(Type actualType, LocalDate content) {
        return content.toString().getBytes();
    }
}
