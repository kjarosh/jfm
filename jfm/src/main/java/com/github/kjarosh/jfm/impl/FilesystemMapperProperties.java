package com.github.kjarosh.jfm.impl;

/**
 * @author Kamil Jarosz
 */
public class FilesystemMapperProperties {
    public static final String MOUNT_IN_DEBUG_MODE =
            "com.github.kjarosh.jfm.mount_in_debug_mode";

    public static boolean mountInDebugMode() {
        return "true".equals(System.getProperty(MOUNT_IN_DEBUG_MODE, "false"));
    }
}
