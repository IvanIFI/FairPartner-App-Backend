package com.ferrinsa.fairpartner.exception.expense.invitation;

import com.ferrinsa.fairpartner.exception.AppException;

public class UserNotInvitedException extends AppException {

    public UserNotInvitedException(String userValue, String groupValue) {
        super("USER_NOT_INVITED",
                String.format("El usuario %s no esta invitado en el grupo %s", userValue, groupValue));
    }
    
}
