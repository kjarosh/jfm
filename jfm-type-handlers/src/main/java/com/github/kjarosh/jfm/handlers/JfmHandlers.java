package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.api.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.api.types.TypeHandler;
import org.reflections.Reflections;

import java.util.stream.Stream;

public class JfmHandlers {
    public static Stream<Class<? extends TypeHandler>> getJfmHandlers() {
        return new Reflections(JfmHandlers.class.getPackage().getName())
                .getSubTypesOf(TypeHandler.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(RegisterTypeHandler.class));
    }
}
