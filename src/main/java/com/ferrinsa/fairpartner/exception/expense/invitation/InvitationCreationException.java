package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class InvitationCreationException extends AppException {

    public InvitationCreationException() {
        super("INVITATION_CANNOT_CREATE","La invitación no se ha podido crear");
    }

}
