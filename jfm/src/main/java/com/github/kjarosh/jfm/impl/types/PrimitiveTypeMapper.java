package com.github.kjarosh.jfm.impl.types;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Kamil Jarosz
 */
public class PrimitiveTypeMapper {
    private static final ConcurrentMap<Type, Type> PRIMITIVE_MAP = new ConcurrentHashMap<>();

    static {
        PRIMITIVE_MAP.put(int.class, Integer.class);
        PRIMITIVE_MAP.put(char.class, Character.class);
        PRIMITIVE_MAP.put(short.class, Short.class);
        PRIMITIVE_MAP.put(long.class, Long.class);
        PRIMITIVE_MAP.put(byte.class, Byte.class);
        PRIMITIVE_MAP.put(boolean.class, Boolean.class);
        PRIMITIVE_MAP.put(float.class, Float.class);
        PRIMITIVE_MAP.put(double.class, Double.class);
        PRIMITIVE_MAP.put(void.class, Void.class);
    }

    public static Type mapPossiblePrimitive(Type possiblePrimitive) {
        Type type = PRIMITIVE_MAP.get(possiblePrimitive);
        return type != null ? type : possiblePrimitive;
    }
}
