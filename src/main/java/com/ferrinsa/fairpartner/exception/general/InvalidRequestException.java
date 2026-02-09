package com.ferrinsa.fairpartner.exception.general;

import com.ferrinsa.fairpartner.exception.AppException;

public class InvalidRequestException extends AppException {

    public InvalidRequestException() {
        super("INVALID_REQUEST", "Los datos enviados no cumplen las validaciones.");
    }
}