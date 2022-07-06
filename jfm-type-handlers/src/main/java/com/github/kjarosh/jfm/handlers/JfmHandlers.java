package com.github.kjarosh.jfm.handlers;

import com.github.kjarosh.jfm.handlers.basic.BooleanTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.ByteArrayTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.ByteTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.CharArrayTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.CharSequenceTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.CharTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.DoubleTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.FloatTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.IntegerTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.OptionalIntTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.OptionalTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.ShortTypeHandler;
import com.github.kjarosh.jfm.handlers.basic.StringTypeHandler;
import com.github.kjarosh.jfm.handlers.complex.PropertiesTypeHandler;
import com.github.kjarosh.jfm.handlers.javatime.DurationTypeHandler;
import com.github.kjarosh.jfm.handlers.javatime.InstantTypeHandler;
import com.github.kjarosh.jfm.handlers.javatime.LocalDateTimeTypeHandler;
import com.github.kjarosh.jfm.handlers.javatime.LocalDateTypeHandler;
import com.github.kjarosh.jfm.handlers.javatime.LocalTimeTypeHandler;
import com.github.kjarosh.jfm.handlers.javatime.OffsetDateTimeTypeHandler;
import com.github.kjarosh.jfm.handlers.javatime.OffsetTimeTypeHandler;
import com.github.kjarosh.jfm.handlers.javatime.YearTypeHandler;
import com.github.kjarosh.jfm.handlers.javatime.ZonedDateTimeTypeHandler;
import com.github.kjarosh.jfm.handlers.listing.FilesystemResourceListListingTypeHandler;
import com.github.kjarosh.jfm.handlers.listing.FilesystemResourceMapListingTypeHandler;
import com.github.kjarosh.jfm.handlers.listing.FilesystemResourceStreamListingTypeHandler;
import com.github.kjarosh.jfm.handlers.listing.StringListListingTypeHandler;
import com.github.kjarosh.jfm.handlers.listing.StringStreamListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandler;

import java.util.stream.Stream;

/**
 * @author Kamil Jarosz
 */
public class JfmHandlers {
    @SuppressWarnings("rawtypes")
    public static Stream<Class<? extends TypeHandler>> getAllTypeHandlers() {
        return Stream.of(
                BooleanTypeHandler.class,
                ByteArrayTypeHandler.class,
                ByteTypeHandler.class,
                CharArrayTypeHandler.class,
                CharSequenceTypeHandler.class,
                CharTypeHandler.class,
                DoubleTypeHandler.class,
                FloatTypeHandler.class,
                IntegerTypeHandler.class,
                OptionalIntTypeHandler.class,
                OptionalTypeHandler.class,
                ShortTypeHandler.class,
                StringTypeHandler.class,

                PropertiesTypeHandler.class,

                DurationTypeHandler.class,
                InstantTypeHandler.class,
                LocalDateTimeTypeHandler.class,
                LocalDateTypeHandler.class,
                LocalTimeTypeHandler.class,
                OffsetDateTimeTypeHandler.class,
                OffsetTimeTypeHandler.class,
                YearTypeHandler.class,
                ZonedDateTimeTypeHandler.class);
    }

    @SuppressWarnings("rawtypes")
    public static Stream<Class<? extends ListingTypeHandler>> getAllListingTypeHandlers() {
        return Stream.of(
                FilesystemResourceMapListingTypeHandler.class,
                FilesystemResourceListListingTypeHandler.class,
                FilesystemResourceStreamListingTypeHandler.class,
                StringListListingTypeHandler.class,
                StringStreamListingTypeHandler.class);
    }
}
