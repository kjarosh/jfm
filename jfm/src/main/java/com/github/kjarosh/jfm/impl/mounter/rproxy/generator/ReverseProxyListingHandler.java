package com.github.kjarosh.jfm.impl.mounter.rproxy.generator;

import com.github.kjarosh.jfm.api.FilesystemMapper;
import com.github.kjarosh.jfm.api.annotations.Listing;
import com.github.kjarosh.jfm.impl.AnnotationHandler;
import com.github.kjarosh.jfm.spi.types.TypeHandlerService;

import java.lang.reflect.Method;
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
        return null;
    }
}
