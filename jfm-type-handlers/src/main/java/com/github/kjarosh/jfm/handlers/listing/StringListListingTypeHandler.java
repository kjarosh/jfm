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

@RegisterTypeHandler
public class StringListListingTypeHandler implements ListingTypeHandler<List<String>> {
    @Override
    public TypeReference<List<String>> getHandledType() {
        return new TypeReference<List<String>>() {
        };
    }

    @Override
    public List<String> list(Type actualType, Path path) throws IOException {
        return Files.list(path)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());
    }
}
