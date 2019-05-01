package com.github.kjarosh.jfm.spi.types;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kamil Jarosz
 */
class TypeReferencesVariablesTest {
    private void assertTypeVariableFound(
            TypeReference<?> withVariable, TypeReference<?> actual,
            TypeReference<?> expected) {
        Optional<Type> variableType = TypeReferences.getTypeFromVariable(
                withVariable,
                TypeReferences.getType(actual),
                "T");

        assertThat(variableType)
                .isPresent()
                .hasValue(TypeReferences.getType(expected));
    }

    private void assertTypeVariableNotFound(
            TypeReference<?> withVariable, TypeReference<?> actual) {
        Optional<Type> variableType = TypeReferences.getTypeFromVariable(
                withVariable,
                TypeReferences.getType(actual),
                "T");

        assertThat(variableType).isNotPresent();
    }

    @Test
    <T> void getTypeFromVariable1() {
        TypeReference<?> withVariable = new TypeReference<Map<String, List<T>>>() {
        };
        TypeReference<?> actual = new TypeReference<Map<String, List<Map<String, String>>>>() {
        };
        TypeReference<?> expectedVariable = new TypeReference<Map<String, String>>() {
        };

        assertTypeVariableFound(withVariable, actual, expectedVariable);
    }

    @Test
    <T> void getTypeFromVariable2() {
        TypeReference<?> withVariable = new TypeReference<T[]>() {
        };
        TypeReference<?> actual = new TypeReference<List<String>[]>() {
        };
        TypeReference<?> expectedVariable = new TypeReference<List<String>>() {
        };

        assertTypeVariableFound(withVariable, actual, expectedVariable);
    }

    @Test
    <T> void getTypeFromVariable3() {
        TypeReference<?> withVariable = new TypeReference<List<T[]>>() {
        };
        TypeReference<?> actual = new TypeReference<List<List<?>[]>>() {
        };
        TypeReference<?> expectedVariable = new TypeReference<List<?>>() {
        };

        assertTypeVariableFound(withVariable, actual, expectedVariable);
    }

    @Test
    <T> void getTypeFromVariableIncompatible1() {
        TypeReference<?> withVariable = new TypeReference<List<T>>() {
        };
        TypeReference<?> actual = new TypeReference<Map<String, String>>() {
        };

        assertTypeVariableNotFound(withVariable, actual);
    }

    @Test
    <T> void getTypeFromVariableIncompatible2() {
        TypeReference<?> withVariable = new TypeReference<List<T>>() {
        };
        TypeReference<?> actual = new TypeReference<String>() {
        };

        assertTypeVariableNotFound(withVariable, actual);
    }

    @Test
    <T> void getTypeFromVariableIncompatible3() {
        TypeReference<?> withVariable = new TypeReference<T[]>() {
        };
        TypeReference<?> actual = new TypeReference<String>() {
        };

        assertTypeVariableNotFound(withVariable, actual);
    }

    @Test
    <T> void getTypeFromVariableIncompatible4() {
        TypeReference<?> withVariable = new TypeReference<ArrayList<T>>() {
        };
        TypeReference<?> actual = new TypeReference<List<String>>() {
        };

        assertTypeVariableNotFound(withVariable, actual);
    }

    @Test
    <T> void getTypeFromVariableIncompatible5() {
        TypeReference<?> withVariable = new TypeReference<List<T>>() {
        };
        TypeReference<?> actual = new TypeReference<ArrayList<String>>() {
        };

        assertTypeVariableNotFound(withVariable, actual);
    }

    @Test
    void getTypeFromVariableWithNoVariable1() {
        TypeReference<?> withVariable = new TypeReference<List<String>>() {
        };
        TypeReference<?> actual = new TypeReference<Map<String, String>>() {
        };

        assertTypeVariableNotFound(withVariable, actual);
    }

    @Test
    void getTypeFromVariableWithNoVariable2() {
        TypeReference<?> withVariable = new TypeReference<String>() {
        };
        TypeReference<?> actual = new TypeReference<String>() {
        };

        assertTypeVariableNotFound(withVariable, actual);
    }

    @Test
    <T, S> void getTypeFromVariableWithMultipleVariables() {
        TypeReference<?> withVariables = new TypeReference<Map<T, S>>() {
        };
        TypeReference<?> actual = new TypeReference<Map<Object, String>>() {
        };

        Optional<Type> variableTypeT = TypeReferences.getTypeFromVariable(
                withVariables,
                TypeReferences.getType(actual),
                "T");

        Optional<Type> variableTypeS = TypeReferences.getTypeFromVariable(
                withVariables,
                TypeReferences.getType(actual),
                "S");

        assertThat(variableTypeT)
                .isPresent()
                .hasValue(Object.class);

        assertThat(variableTypeS)
                .isPresent()
                .hasValue(String.class);
    }

    @Test
    <T> void getTypeFromVariableWithDuplicateVariable() {
        TypeReference<?> withVariable = new TypeReference<Map<T, T>>() {
        };
        TypeReference<?> actual = new TypeReference<Map<String, String>>() {
        };

        Optional<Type> variableType = TypeReferences.getTypeFromVariable(
                withVariable,
                TypeReferences.getType(actual),
                "T");

        assertThat(variableType)
                .isPresent()
                .hasValue(String.class);
    }

    @Test
    <T> void getTypeFromVariableWithInvalidDuplicateVariable() {
        TypeReference<?> withVariable = new TypeReference<Map<T, T>>() {
        };
        TypeReference<?> actual = new TypeReference<Map<String, Object>>() {
        };

        Optional<Type> variableType = TypeReferences.getTypeFromVariable(
                withVariable,
                TypeReferences.getType(actual),
                "T");

        assertThat(variableType)
                .isNotPresent();
    }
}