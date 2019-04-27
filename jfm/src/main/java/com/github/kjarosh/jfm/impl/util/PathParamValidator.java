package com.github.kjarosh.jfm.impl.util;

import com.github.kjarosh.jfm.api.FilesystemMapperException;
import com.github.kjarosh.jfm.api.annotations.PathParam;

import java.io.File;

/**
 * @author Kamil Jarosz
 */
public class PathParamValidator {
    public void validate(PathParam pathParam, String value) {
        if (!pathParam.allowSeparators() &&
                (value.contains("/") || value.contains(File.separator))) {
            throw new FilesystemMapperException(
                    "Parameter value contains separators: " + value);
        }

        if (!value.matches(pathParam.regex())) {
            throw new FilesystemMapperException(
                    "Parameter value is not compliant with regular expression: " + value
                            + ", regex: " + pathParam.regex());
        }
    }
}
