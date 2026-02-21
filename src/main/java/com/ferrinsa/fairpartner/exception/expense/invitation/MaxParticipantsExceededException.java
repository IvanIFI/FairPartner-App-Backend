package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class MaxParticipantsExceededException extends AppException {

    public MaxParticipantsExceededException() {
        super("MAX_PARTICIPANTS_EXCEEDED", "No se admiten más participantes.");
    }


}
