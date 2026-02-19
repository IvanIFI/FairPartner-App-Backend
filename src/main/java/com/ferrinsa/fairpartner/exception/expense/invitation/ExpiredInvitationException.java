package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class ExpiredInvitationException extends AppException {

    public ExpiredInvitationException() {
        super("EXPIRED_INVITATION", "La invitación esta expirada");
    }

}
