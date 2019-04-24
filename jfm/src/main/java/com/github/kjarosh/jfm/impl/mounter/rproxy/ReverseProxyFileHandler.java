package com.github.kjarosh.jfm.impl.mounter.rproxy;

/**
 * @author Kamil Jarosz
 */
/*public class ReverseProxyFileHandler {
    private final AnnotationHandlingService annotationHandlingService = new AnnotationHandlingService();
    private final List<Method> methods = new ArrayList<>();
    private final String path;
    private final Object resource;

    ReverseProxyFileHandler(String path, Object resource) {
        this.path = path;
        this.resource = resource;
    }

    public byte[] read() {
        return handle("read", method -> new ReverseProxyReadHandler(method, resource));
    }

    public void write(byte[] data) {
        handle("write", method -> new ReverseProxyWriteHandler(method, resource, data));
    }

    public void delete() {
        handle("delete", method -> new ReverseProxyDeleteHandler(method, resource));
    }

    private <T> T handle(String what, Function<Method, AnnotationHandler<T>> handler) {
        for (Method method : methods) {
            try {
                return annotationHandlingService.handle(method, handler.apply(method));
            } catch (UnsupportedAnnotationException e) {
                continue;
            }
        }

        throw new NoHandlerMethodException("No method to handle " + what + ": " + path);
    }

    public void addHandlingMethod(Method method) {
        methods.add(method);
    }
}*/
