package com.ferrinsa.fairpartner.exception.general;

import com.ferrinsa.fairpartner.exception.AppException;

public class InvalidRequestException extends AppException {

    public InvalidRequestException(String fieldName, String message) {
        super("INVALID_REQUEST", String.format("Campo %s incorrecto, causa: %s", fieldName, message));
    }

    public InvalidRequestException(String fieldName, int maxSize) {
        super("INVALID_REQUEST", String.format("Campo %s incorrecto, causa: Tamaño máximo %s", fieldName, maxSize));
    }
}