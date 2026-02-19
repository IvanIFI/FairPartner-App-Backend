package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class InvitationAlreadyExistsException extends AppException {

    public InvitationAlreadyExistsException(String userValue, String expenseGroupValue) {
        super("INVITATION_ALREADY_EXISTS",
                String.format("El usuario %s tiene ya una invitación pendiente en el grupo %s",
                        userValue, expenseGroupValue));
    }
}
