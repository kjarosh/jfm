package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;
import org.reflections.Reflections;

import java.util.stream.Stream;

public class JfmHandlers {
    public static Stream<Class<? extends TypeHandler>> getJfmHandlers() {
        return new Reflections(JfmHandlers.class.getPackage().getName())
                .getSubTypesOf(TypeHandler.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(RegisterTypeHandler.class));
    }

    public static Stream<Class<? extends ListingTypeHandler>> getJfmListingHandlers() {
        return new Reflections(JfmHandlers.class.getPackage().getName())
                .getSubTypesOf(ListingTypeHandler.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(RegisterTypeHandler.class));
    }
}
