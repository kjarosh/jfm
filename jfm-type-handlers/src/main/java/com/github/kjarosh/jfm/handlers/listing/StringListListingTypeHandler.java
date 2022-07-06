package com.github.kjarosh.jfm.handlers.listing;

import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.RegisterTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeReference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kamil Jarosz
 */
@RegisterTypeHandler
public class StringListListingTypeHandler implements ListingTypeHandler<List<String>> {
    @Override
    public TypeReference<List<String>> getHandledType() {
        return new TypeReference<List<String>>() {
        };
    }

    @Override
    public List<String> list(Type actualType, Path path) throws IOException {
        try (Stream<Path> list = Files.list(path)) {
            return list
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<String> itemize(Type actualType, List<String> list) {
        return list;
    }
}
