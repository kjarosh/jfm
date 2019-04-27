package com.github.kjarosh.jfm.impl.mounter.rproxy.generator;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.impl.AnnotationHandler;
import com.github.kjarosh.jfm.spi.types.ListingTypeHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * @author Kamil Jarosz
 */
public class ReverseProxyListingHandler implements AnnotationHandler<List<String>> {
    private final TypeHandlerService typeHandlerService = FilesystemMapper.instance()
            .getTypeHandlerService();
    private final ResourceMethodInvoker invoker;

    ReverseProxyListingHandler(Method method, Object resource) {
        this.invoker = new ResourceMethodInvoker(method, resource);
    }

    @Override
    public List<String> handleListing(Listing listing) {
        return handleListing0();
    }

    @SuppressWarnings("unchecked")
    private <T> List<String> handleListing0() {
        Object readObject = invoker.invoke();
        if (readObject == null) {
            return Collections.emptyList();
        }

        Type type = invoker.getReturnType();
        ListingTypeHandler<T> returnTypeHandler = (ListingTypeHandler<T>) typeHandlerService.getListingHandlerFor(type);
        return returnTypeHandler.itemize(type, (T) readObject);
    }
}
