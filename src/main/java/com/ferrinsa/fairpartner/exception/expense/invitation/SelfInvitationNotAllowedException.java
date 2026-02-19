package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class SelfInvitationNotAllowedException extends AppException {

    public SelfInvitationNotAllowedException() {
        super("SELF_INVITATION_NOT_ALLOWED", "Un usuario no puede invitarse a si mismo");
    }
}
