package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class InvitationNotFoundException extends AppException {

    public InvitationNotFoundException() {
        super("INVITATION_NOT_FOUND", "No se ha encontrado la invitación");
    }

}
