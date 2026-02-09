package com.ferrinsa.fairpartner.utils;

import com.ferrinsa.fairpartner.exception.general.InvalidRequestException;

public class CheckUtils {

    private CheckUtils() {
        // Utility class, this constructor prevents instantiation
    }

    public static void isValidSize(int size, String field) {
        boolean isValid = field != null && field.length() <= size && !field.isBlank();

        if (!isValid) {
            throw new InvalidRequestException();
        }
    }

}
