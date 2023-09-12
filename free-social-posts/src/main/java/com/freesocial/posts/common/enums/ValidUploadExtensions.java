package com.freesocial.posts.common.enums;

import java.util.Arrays;

public enum ValidUploadExtensions {

    PNG,
    JPG,
    JPEG,
    AVI,
    MP4;

    /**
     * Extension is valid if its name is found
     *
     * @param extension the file extension
     * @return true if extension exists in this enum
     */
    public static boolean isExtensionValid(String extension) {
        return Arrays.stream(ValidUploadExtensions.values()).map(ValidUploadExtensions::name)
                .anyMatch(e -> e.equalsIgnoreCase(extension));
    }

}
