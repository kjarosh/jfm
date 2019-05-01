package com.github.kjarosh.jfm.spi.types;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Kamil Jarosz
 */
class TypeReferencesSimilarityTest {
    private void tryGetSimilarity(
            TypeReference<?> actualType, TypeReference<?> typeTemplate) {
        TypeReferences.calculateSimilarity(
                TypeReferences.getType(actualType), TypeReferences.getType(typeTemplate));
    }

    private int getSimilarity(
            TypeReference<?> actualType, TypeReference<?> typeTemplate) {
        Optional<Integer> similarity = TypeReferences.calculateSimilarity(
                TypeReferences.getType(actualType), TypeReferences.getType(typeTemplate));

        assertThat(similarity).isPresent();

        return similarity.get();

    }

    private void assertNoSimilarity(
            TypeReference<?> actualType, TypeReference<?> typeTemplate) {
        Optional<Integer> similarity = TypeReferences.calculateSimilarity(
                TypeReferences.getType(actualType), TypeReferences.getType(typeTemplate));

        assertThat(similarity).isNotPresent();
    }

    @Test
    <T> void testInvalidActualType() {
        // this needs to be here because of a Java bug
        TypeReference<T> actualType = new TypeReference<T>() {
        };

        assertThatThrownBy(() -> tryGetSimilarity(
                actualType,
                new TypeReference<String>() {
                }))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Actual type has variables");
    }

    @Test
    void testExactSimilarity() {
        assertThat(getSimilarity(
                new TypeReference<String>() {
                },
                new TypeReference<String>() {
                })).isEqualTo(1);
    }

    @Test
    void testExactParameterizedSimilarity() {
        assertThat(getSimilarity(
                new TypeReference<List<String>>() {
                },
                new TypeReference<List<String>>() {
                })).isEqualTo(2);

        assertThat(getSimilarity(
                new TypeReference<List<List<String>>>() {
                },
                new TypeReference<List<List<String>>>() {
                })).isEqualTo(3);
    }

    @Test
    void testExactArraySimilarity() {
        assertThat(getSimilarity(
                new TypeReference<String[]>() {
                },
                new TypeReference<String[]>() {
                })).isEqualTo(2);

        assertThat(getSimilarity(
                new TypeReference<String[][]>() {
                },
                new TypeReference<String[][]>() {
                })).isEqualTo(3);
    }

    @Test
    <T> void testBasicSimilarity() {
        assertThat(getSimilarity(
                new TypeReference<String>() {
                },
                new TypeReference<T>() {
                })).isEqualTo(0);
    }

    @Test
    <T> void testBasicParameterizedSimilarity1() {
        assertThat(getSimilarity(
                new TypeReference<List<List<String>>>() {
                },
                new TypeReference<T>() {
                })).isEqualTo(0);
    }

    @Test
    <T> void testBasicParameterizedSimilarity2() {
        assertThat(getSimilarity(
                new TypeReference<List<List<String>>>() {
                },
                new TypeReference<List<T>>() {
                })).isEqualTo(1);
    }

    @Test
    <T> void testBasicParameterizedSimilarity3() {
        assertThat(getSimilarity(
                new TypeReference<List<List<String>>>() {
                },
                new TypeReference<List<List<T>>>() {
                })).isEqualTo(2);
    }

    @Test
    <T> void testBasicArraySimilarity1() {
        assertThat(getSimilarity(
                new TypeReference<String[][]>() {
                },
                new TypeReference<T>() {
                })).isEqualTo(0);
    }

    @Test
    <T> void testBasicArraySimilarity2() {
        assertThat(getSimilarity(
                new TypeReference<String[][]>() {
                },
                new TypeReference<T[]>() {
                })).isEqualTo(1);
    }

    @Test
    <T> void testBasicArraySimilarity3() {
        assertThat(getSimilarity(
                new TypeReference<String[][]>() {
                },
                new TypeReference<T[][]>() {
                })).isEqualTo(2);
    }

    @Test
    <T, Y> void testMultipleParameterizedSimilarity1() {
        assertThat(getSimilarity(
                new TypeReference<Map<String, Object>>() {
                },
                new TypeReference<Map<T, Y>>() {
                })).isEqualTo(1);
    }

    @Test
    <T, Y> void testMultipleParameterizedSimilarity2() {
        assertThat(getSimilarity(
                new TypeReference<Map<List<String>, Object>>() {
                },
                new TypeReference<Map<List<T>, Y>>() {
                })).isEqualTo(2);
    }

    @Test
    <T, Y> void testMultipleParameterizedSimilarity3() {
        assertThat(getSimilarity(
                new TypeReference<Map<String, List<Object>>>() {
                },
                new TypeReference<Map<T, List<Y>>>() {
                })).isEqualTo(2);
    }

    @Test
    <T, Y> void testMultipleParameterizedSimilarity4() {
        assertThat(getSimilarity(
                new TypeReference<Map<List<String>, List<Object>>>() {
                },
                new TypeReference<Map<List<T>, List<Y>>>() {
                })).isEqualTo(3);
    }

    @Test
    <T> void testIncompatibleSimilarity1() {
        assertNoSimilarity(
                new TypeReference<String>() {
                },
                new TypeReference<List<List<T>>>() {
                });

        assertNoSimilarity(
                new TypeReference<ArrayList<String>>() {
                },
                new TypeReference<List<String>>() {
                });

        assertNoSimilarity(
                new TypeReference<String[]>() {
                },
                new TypeReference<Object>() {
                });
    }

    @Test
    <T, Y> void testIncompatibleSimilarity2() {
        assertNoSimilarity(
                new TypeReference<Map<String, Object>>() {
                },
                new TypeReference<Map<List<T>, Y>>() {
                });
    }

    @Test
    <T> void testIncompatibleSimilarity3() {
        assertNoSimilarity(
                new TypeReference<String[]>() {
                },
                new TypeReference<T[][]>() {
                });
    }

    @Test
    <T> void testIncompatibleSimilarity4() {
        assertNoSimilarity(
                new TypeReference<String[]>() {
                },
                new TypeReference<String[][]>() {
                });
    }

    @Test
    <T> void testIncompatibleSimilarity5() {
        assertNoSimilarity(
                new TypeReference<List<String>>() {
                },
                new TypeReference<T[]>() {
                });
    }

    @Test
    void testIncompatibleSimilarity6() {
        assertNoSimilarity(
                new TypeReference<List<String>>() {
                },
                new TypeReference<String>() {
                });
    }
}
