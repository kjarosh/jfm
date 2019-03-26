package com.github.kjarosh.jfm.api;

import org.reflections.Reflections;

import java.util.Set;

class FilesystemMapperProvider {
    private static final Object instanceLock = new Object();
    private static final Reflections jfmReflections = new Reflections("com.github.kjarosh.jfm");

    private static FilesystemMapper instance;

    static FilesystemMapper getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    return instance = loadInstance();
                }
            }
        }

        return instance;
    }

    private static FilesystemMapper loadInstance() {
        Set<Class<? extends FilesystemMapper>> implementations =
                jfmReflections.getSubTypesOf(FilesystemMapper.class);

        if (implementations.size() == 0) {
            throw new ImplementationLookupException("No implementation found");
        }

        if (implementations.size() > 1) {
            throw new ImplementationLookupException("More than one implementation found: " + implementations);
        }

        try {
            return implementations.iterator().next().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ImplementationLookupException("Cannot instantiate impl class", e);
        }
    }
}
