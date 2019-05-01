package com.github.kjarosh.jfm.spi.types;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Kamil Jarosz
 */
public class TypeReferences {
    private static final Function<Integer, Integer> increaseSimilarity = i -> i + 1;

    public static <T> Type getType(TypeReference<T> ref) {
        Type[] genericInterfaces = ((TypeReference<?>) ref).getClass().getGenericInterfaces();

        if (genericInterfaces.length != 1 || !(genericInterfaces[0] instanceof ParameterizedType)) {
            throw new InvalidTypeReferenceException();
        }

        ParameterizedType genericInterface =
                (ParameterizedType) genericInterfaces[0];
        return genericInterface.getActualTypeArguments()[0];
    }

    public static Optional<Type> getTypeFromVariable(TypeReference<?> reference, Type actualType, String variableName) {
        return getTypeFromVariable(getType(reference), actualType, variableName);
    }

    public static Optional<Type> getTypeFromVariable(Type typeTemplate, Type actualType, String variableName) {
        Objects.requireNonNull(typeTemplate);
        Objects.requireNonNull(actualType);
        Objects.requireNonNull(variableName);

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
        } else {
            return Optional.empty();
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

        Set<Type> found = new HashSet<>();
        for (int i = 0; i < argsCount; ++i) {
            Optional<Type> ret = getTypeFromVariable(newTypeTemplates[i], newActualTypes[i], variableName);
            ret.ifPresent(found::add);
        }

        if (found.size() == 1) {
            return Optional.of(found.iterator().next());
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Integer> calculateSimilarity(Type actual, Type template) {
        Objects.requireNonNull(actual);
        Objects.requireNonNull(template);

        if (actual instanceof TypeVariable) {
            throw new IllegalArgumentException("Actual type has variables");
        }

        if (template instanceof TypeVariable) {
            return Optional.of(0);
        }

        if (template instanceof GenericArrayType) {
            if (!(actual instanceof Class<?>)) {
                return Optional.empty();
            }

            return calculateSimilarity((Class<?>) actual, (GenericArrayType) template);
        } else if (template instanceof Class<?>) {
            if (!(actual instanceof Class<?>)) {
                return Optional.empty();
            }

            return calculateSimilarity((Class<?>) actual, (Class<?>) template);
        } else if (template instanceof ParameterizedType) {
            if (!(actual instanceof ParameterizedType)) {
                return Optional.empty();
            }

            return calculateSimilarity((ParameterizedType) actual, (ParameterizedType) template);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> calculateSimilarity(Class<?> actual, Class<?> template) {
        Objects.requireNonNull(actual);
        Objects.requireNonNull(template);

        if (template.isArray()) {
            if (!actual.isArray()) {
                return Optional.empty();
            }

            return calculateSimilarity(actual.getComponentType(), template.getComponentType())
                    .map(increaseSimilarity);
        }

        if (actual == template) {
            return Optional.of(1);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> calculateSimilarity(Class<?> actual, GenericArrayType template) {
        Objects.requireNonNull(actual);
        Objects.requireNonNull(template);

        if (!actual.isArray()) {
            return Optional.empty();
        }

        return calculateSimilarity(
                actual.getComponentType(),
                template.getGenericComponentType())
                .map(increaseSimilarity);
    }

    private static Optional<Integer> calculateSimilarity(ParameterizedType actual, ParameterizedType template) {
        Objects.requireNonNull(actual);
        Objects.requireNonNull(template);

        if (!calculateSimilarity(actual.getRawType(), template.getRawType()).isPresent()) {
            return Optional.empty();
        }

        int parametersCount = actual.getActualTypeArguments().length;
        if (parametersCount != template.getActualTypeArguments().length) {
            return Optional.empty();
        }

        int ret = 1;
        for (int i = 0; i < parametersCount; ++i) {
            Optional<Integer> similarity = calculateSimilarity(
                    actual.getActualTypeArguments()[i],
                    template.getActualTypeArguments()[i]);
            if (similarity.isPresent()) {
                ret += similarity.get();
            } else {
                return Optional.empty();
            }
        }

        return Optional.of(ret);
    }
}
