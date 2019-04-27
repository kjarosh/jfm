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
public class StringStreamListingTypeHandler implements ListingTypeHandler<Stream<String>> {
    @Override
    public TypeReference<Stream<String>> getHandledType() {
        return new TypeReference<Stream<String>>() {
        };
    }

    @Override
    public Stream<String> list(Type actualType, Path path) throws IOException {
        return Files.list(path)
                .map(Path::getFileName)
                .map(Path::toString);
    }

    @Override
    public List<String> itemize(Type actualType, Stream<String> stream) {
        return stream.collect(Collectors.toList());
    }
}
