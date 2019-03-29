package com.github.kjarosh.jfm.spi.types;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Optional;

/**
 * @author Kamil Jarosz
 */
public class TypeReferences {
    public static <T> Type getType(TypeReference<T> ref) {
        Type[] genericInterfaces = ((TypeReference<?>) ref).getClass().getGenericInterfaces();

        if (genericInterfaces.length != 1 || !(genericInterfaces[0] instanceof ParameterizedType)) {
            throw new InvalidTypeReferenceException();
        }

        ParameterizedType genericInterface =
                (ParameterizedType) genericInterfaces[0];
        return genericInterface.getActualTypeArguments()[0];
    }

    public static <T> Optional<Type> getTypeFromVariable(TypeReference<T> reference, Type actualType, String variableName) {
        return getTypeFromVariable(getType(reference), actualType, variableName);
    }

    public static Optional<Type> getTypeFromVariable(Type typeTemplate, Type actualType, String variableName) {
        if (typeTemplate instanceof TypeVariable) {
            if (((TypeVariable) typeTemplate).getName().equals(variableName)) {
                return Optional.of(actualType);
            } else {
                return Optional.empty();
            }
        }

        if (typeTemplate instanceof Class<?>) {
            return Optional.empty();
        } else if (typeTemplate instanceof GenericArrayType) {
            if (!(actualType instanceof GenericArrayType)) {
                return Optional.empty();
            }

            Type newTypeTemplate = ((GenericArrayType) typeTemplate).getGenericComponentType();
            Type newActualType = ((GenericArrayType) actualType).getGenericComponentType();
            return getTypeFromVariable(newTypeTemplate, newActualType, variableName);
        } else if (typeTemplate instanceof ParameterizedType) {
            if (!(actualType instanceof ParameterizedType)) {
                return Optional.empty();
            }

            return getTypeFromVariable((ParameterizedType) typeTemplate, (ParameterizedType) actualType, variableName);
        } else if (typeTemplate instanceof WildcardType) {
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static Optional<Type> getTypeFromVariable(ParameterizedType typeTemplate, ParameterizedType actualType, String variableName) {
        if (!typeTemplate.getRawType().equals(actualType.getRawType())) {
            return Optional.empty();
        }

        Type[] newTypeTemplates = typeTemplate.getActualTypeArguments();
        Type[] newActualTypes = actualType.getActualTypeArguments();

        int argsCount = newTypeTemplates.length;
        if (argsCount != newActualTypes.length) {
            return Optional.empty();
        }

        for (int i = 0; i < argsCount; ++i) {
            Optional<Type> ret = getTypeFromVariable(newTypeTemplates[i], newActualTypes[i], variableName);
            if (ret.isPresent()) return ret;
        }

        return Optional.empty();
    }

    public static Optional<Integer> calculateSimilarity(Type type, Type other) {
        if (type instanceof TypeVariable || other instanceof TypeVariable) {
            return Optional.of(1);
        }

        if (type instanceof Class<?>) {
            if (!(other instanceof Class<?>)) {
                return Optional.empty();
            }

            return calculateSimilarity((Class<?>) type, (Class<?>) other);
        } else if (type instanceof GenericArrayType) {
            if (!(other instanceof GenericArrayType)) {
                return Optional.empty();
            }

            return calculateSimilarity((GenericArrayType) type, (GenericArrayType) other);
        } else if (type instanceof ParameterizedType) {
            if (!(other instanceof ParameterizedType)) {
                return Optional.empty();
            }

            return calculateSimilarity((ParameterizedType) type, (ParameterizedType) other);
        } else if (type instanceof WildcardType) {
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static Optional<Integer> calculateSimilarity(Class<?> type, Class<?> other) {
        if (type == other) {
            return Optional.of(1);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> calculateSimilarity(GenericArrayType type, GenericArrayType other) {
        return calculateSimilarity(
                type.getGenericComponentType(),
                other.getGenericComponentType())
                .map(i -> i + 1);
    }

    private static Optional<Integer> calculateSimilarity(ParameterizedType type, ParameterizedType other) {
        if (!calculateSimilarity(type.getRawType(), other.getRawType()).isPresent()) {
            return Optional.empty();
        }

        int parametersCount = type.getActualTypeArguments().length;
        if (parametersCount != other.getActualTypeArguments().length) {
            return Optional.empty();
        }

        int ret = 1;
        for (int i = 0; i < parametersCount; ++i) {
            Optional<Integer> similarity = calculateSimilarity(
                    type.getActualTypeArguments()[i],
                    other.getActualTypeArguments()[i]);
            if (similarity.isPresent()) {
                ret += similarity.get();
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(ret);
    }
}
