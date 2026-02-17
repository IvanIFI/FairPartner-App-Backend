package com.ferrinsa.fairpartner.utils;

import com.ferrinsa.fairpartner.exception.general.InvalidRequestException;

public class CheckUtils {

    private CheckUtils() {
        // Utility class, this constructor prevents instantiation
    }

    public static void isValidSize(String fieldName, String value, int maxSize) {
        if (value == null) {
            throw new InvalidRequestException(fieldName, "Parámetro null");
        }

        if (value.length() > maxSize) {
            throw new InvalidRequestException(fieldName, maxSize);
        }

        if (value.isBlank()) {
            throw new InvalidRequestException(fieldName, "Parámetro en blanco");
        }
    }

}
