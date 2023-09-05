package com.freesocial.posts.common.enums;

import java.util.Arrays;

public enum ValidExtensions {

    PNG,
    JPG,
    JPEG,
    AVI,
    MP4;

    public static boolean isExtensionValid(String extension) {
        return Arrays.stream(ValidExtensions.values()).map(ValidExtensions::name)
                .anyMatch(e -> e.equalsIgnoreCase(extension));
    }

}
