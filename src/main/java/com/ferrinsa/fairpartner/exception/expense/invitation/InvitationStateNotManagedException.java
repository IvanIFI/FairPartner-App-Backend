package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class InvitationStateNotManagedException extends AppException {

    public InvitationStateNotManagedException() {
        super("INVITATION_STATE_NOT_MANAGED", "Estado inesperado no manejado de la invitación");
    }

}
