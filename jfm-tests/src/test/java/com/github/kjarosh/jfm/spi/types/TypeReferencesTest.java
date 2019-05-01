package com.github.kjarosh.jfm.spi.types;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Kamil Jarosz
 */
class TypeReferencesTest {
    @Test
    void getTypeString() {
        Type type = TypeReferences.getType(new TypeReference<String>() {
        });

        assertThat(type)
                .isEqualTo(String.class);
    }

    @Test
    void getTypeList() {
        Type type = TypeReferences.getType(new TypeReference<List<Object>>() {
        });

        assertThat(type)
                .isInstanceOf(ParameterizedType.class);

        ParameterizedType parameterizedType = (ParameterizedType) type;

        assertThat(parameterizedType)
                .extracting(ParameterizedType::getRawType)
                .isEqualTo(List.class);

        assertThat(parameterizedType)
                .extracting(ParameterizedType::getActualTypeArguments)
                .isEqualTo(new Class[]{Object.class});
    }

    @Test
    void getTypeMap() {
        Type type = TypeReferences.getType(new TypeReference<Map<String, Object>>() {
        });

        assertThat(type)
                .isInstanceOf(ParameterizedType.class);

        ParameterizedType parameterizedType = (ParameterizedType) type;

        assertThat(parameterizedType)
                .extracting(ParameterizedType::getRawType)
                .isEqualTo(Map.class);

        assertThat(parameterizedType)
                .extracting(ParameterizedType::getActualTypeArguments)
                .isEqualTo(new Class[]{String.class, Object.class});
    }

    @Test
    void getTypeInvalid() {
        assertThatThrownBy(() -> TypeReferences.getType(new InvalidTypeReference()))
                .isInstanceOf(InvalidTypeReferenceException.class);
    }
}