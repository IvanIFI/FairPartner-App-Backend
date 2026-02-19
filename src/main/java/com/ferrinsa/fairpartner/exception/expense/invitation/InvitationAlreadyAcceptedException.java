package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class InvitationAlreadyAcceptedException extends AppException {

    public InvitationAlreadyAcceptedException(String userValue, String expenseGroupValue) {
        super("INVITATION_ALREADY_ACCEPTED",
                String.format("El usuario %s ya pertenece al grupo %s", userValue, expenseGroupValue));
    }
}
