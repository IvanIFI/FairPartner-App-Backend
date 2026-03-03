package com.ferrinsa.fairpartner.exception.balance;

import com.ferrinsa.fairpartner.exception.AppException;

public class UnsupportedParticipantsNumberException extends AppException {

    public UnsupportedParticipantsNumberException(String value) {
        super(
                "UNSUPPORTED_PARTICIPANTS_NUMBER",
                String.format("Número de participantes incorrecto (%s). Solo se permiten 1 o 2 participantes.", value)
        );
    }

}
